package com.thread.sns.service;

import com.thread.sns.model.Article;
import com.thread.sns.model.ArticleCategory;
import com.thread.sns.model.Category;
import com.thread.sns.repository.ArticleCategoryRepository;
import com.thread.sns.repository.ArticleRepository;
import com.thread.sns.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleCategoryRepository articleCategoryRepository;

    public ArticleService(ArticleRepository articleRepository, CategoryRepository categoryRepository, ArticleCategoryRepository articleCategoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.articleCategoryRepository = articleCategoryRepository;
    }

    @Transactional
    public Article createArticle(String title, String content, List<Long> categoryIds) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);

        for (Long categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            article.addCategory(category);
        }

        return articleRepository.save(article);
    }

    @Transactional
    public Article updateArticle(Long articleId, String title, String content, List<Long> categoryIds) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article ID"));

        article.setTitle(title);
        article.setContent(content);

        // 기존 카테고리 목록 가져오기
        Set<ArticleCategory> existingArticleCategories = article.getArticleCategories();
        Set<Long> existingCategoryIds = existingArticleCategories.stream()
                .map(ac -> ac.getCategory().getId())
                .collect(Collectors.toSet());
        log.info("existingCategoryIds.toString(): {}", existingCategoryIds);
        log.info("existingArticleCategories.toString(): {}", existingArticleCategories);


        // 삭제할 카테고리 찾기
        List<Long> categoriesToRemove = existingCategoryIds.stream()
                .filter(id -> !categoryIds.contains(id))
                .toList();
        log.info("categoriesToRemove.toString(): {}", categoriesToRemove);


        // 추가할 카테고리 찾기
        List<Long> categoriesToAdd = categoryIds.stream()
                .filter(id -> !existingCategoryIds.contains(id))
                .toList();
        log.info("categoriesToAdd.toString(): {}", categoriesToAdd);


        // 카테고리 삭제
        for (Long categoryId : categoriesToRemove) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            article.removeCategory(category);
        }

        // 카테고리 추가
        for (Long categoryId : categoriesToAdd) {
            System.out.println("categoryId = " + categoryId);
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
            article.addCategory(category);
        }

        return articleRepository.save(article);
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article ID"));
        articleRepository.delete(article);
    }

    @Transactional(readOnly = true)
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid article ID"));
    }
}