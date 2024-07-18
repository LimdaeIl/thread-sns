package com.thread.sns.model.dto;

import com.thread.sns.model.ArticleCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCategoryDto {
    private Long id;
    private CategoryDto category;

    public static ArticleCategoryDto fromEntity(ArticleCategory articleCategory) {
        return new ArticleCategoryDto(articleCategory.getId(), CategoryDto.fromEntity(articleCategory.getCategory()));
    }
}