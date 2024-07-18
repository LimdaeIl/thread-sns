package com.thread.sns.service;

import com.thread.sns.model.Article;
import com.thread.sns.model.Category;
import com.thread.sns.repository.ArticleRepository;
import com.thread.sns.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    private Category category1;
    private Category category2;
    private Category category3;

    @BeforeEach
    public void setUp() {
        category1 = new Category();
        category1.setName("Category 1");
        categoryRepository.save(category1);

        category2 = new Category();
        category2.setName("Category 2");
        categoryRepository.save(category2);

        category3 = new Category();
        category3.setName("Category 3");
        categoryRepository.save(category3);
    }

    @Test
    public void testCreateArticle() {
        List<Long> categoryIds = Arrays.asList(category1.getId(), category2.getId());
        Article article = articleService.createArticle("Test Title", "Test Content", categoryIds);

        assertThat(article.getId()).isNotNull();
        assertThat(article.getTitle()).isEqualTo("Test Title");
        assertThat(article.getContent()).isEqualTo("Test Content");
        assertThat(article.getArticleCategories()).hasSize(2);
    }

    @Test
    public void testUpdateArticle() {
        List<Long> initialCategoryIds = Arrays.asList(category1.getId(), category2.getId());
        Article article = articleService.createArticle("Initial Title", "Initial Content", initialCategoryIds);

        List<Long> updatedCategoryIds = Arrays.asList(category2.getId(), category3.getId());
        Article updatedArticle = articleService.updateArticle(article.getId(), "Updated Title", "Updated Content", updatedCategoryIds);

        assertThat(updatedArticle.getId()).isEqualTo(article.getId());
        assertThat(updatedArticle.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedArticle.getContent()).isEqualTo("Updated Content");
        assertThat(updatedArticle.getArticleCategories()).hasSize(2);
        assertThat(updatedArticle.getArticleCategories()).extracting("category.id")
                .containsExactlyInAnyOrder(category2.getId(), category3.getId());
    }

    @Test
    public void testDeleteArticle() {
        List<Long> categoryIds = Arrays.asList(category1.getId(), category2.getId());
        Article article = articleService.createArticle("Test Title", "Test Content", categoryIds);

        articleService.deleteArticle(article.getId());

        Optional<Article> deletedArticle = articleRepository.findById(article.getId());
        assertThat(deletedArticle).isEmpty();
    }
}