package com.thread.sns.repository;

import com.thread.sns.model.Article;
import com.thread.sns.model.ArticleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, Long> {
    List<ArticleCategory> findByArticleId(Long articleId);
}