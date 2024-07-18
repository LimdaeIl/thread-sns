package com.thread.sns.model.dto;

import com.thread.sns.model.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private List<ArticleCategoryDto> articleCategories;

    public static ArticleDto fromEntity(Article article) {
        List<ArticleCategoryDto> categories = article.getArticleCategories().stream()
                .map(ArticleCategoryDto::fromEntity)
                .collect(Collectors.toList());
        return new ArticleDto(article.getId(), article.getTitle(), article.getContent(), categories);
    }
}