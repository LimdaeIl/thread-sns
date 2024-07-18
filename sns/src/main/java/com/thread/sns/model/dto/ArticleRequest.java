package com.thread.sns.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleRequest {
    private String title;
    private String content;
    private List<Long> categoryIds;

    // getters and setters
}