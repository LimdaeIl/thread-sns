package com.thread.sns.controller;

import com.thread.sns.model.Article;
import com.thread.sns.model.dto.ArticleDto;
import com.thread.sns.model.dto.ArticleRequest;
import com.thread.sns.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<ArticleDto> createArticle(@RequestBody ArticleRequest request) {
        Article article = articleService.createArticle(request.getTitle(), request.getContent(), request.getCategoryIds());
        return ResponseEntity.ok(ArticleDto.fromEntity(article));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable Long id, @RequestBody ArticleRequest request) {
        Article article = articleService.updateArticle(id, request.getTitle(), request.getContent(), request.getCategoryIds());
        return ResponseEntity.ok(ArticleDto.fromEntity(article));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticle(@PathVariable Long id) {
        Article article = articleService.findById(id);
        return ResponseEntity.ok(ArticleDto.fromEntity(article));
    }

}