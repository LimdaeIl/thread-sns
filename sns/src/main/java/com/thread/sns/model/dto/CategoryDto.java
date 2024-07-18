package com.thread.sns.model.dto;

import com.thread.sns.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;

    public static CategoryDto fromEntity(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}