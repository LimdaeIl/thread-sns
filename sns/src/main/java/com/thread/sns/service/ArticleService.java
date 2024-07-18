package com.thread.sns.service;

import com.thread.sns.model.Article;
import com.thread.sns.model.Category;
import com.thread.sns.repository.ArticleRepository;
import com.thread.sns.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    public ArticleService(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
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

        // 기존 카테고리 제거
        article.removeAllCategories();

        // 새로운 카테고리 추가
        for (Long categoryId : categoryIds) {
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