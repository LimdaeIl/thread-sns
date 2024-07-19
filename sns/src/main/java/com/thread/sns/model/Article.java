package com.thread.sns.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ArticleCategory> articleCategories = new HashSet<>();

    public void addCategory(Category category) {
        ArticleCategory articleCategory = new ArticleCategory(this, category);
        articleCategories.add(articleCategory);
    }

    public void removeCategory(Category category) {
        for (Iterator<ArticleCategory> iterator = articleCategories.iterator(); iterator.hasNext();) {
            ArticleCategory articleCategory = iterator.next();
            if (articleCategory.getCategory().equals(category)) {
                iterator.remove();
                articleCategory.getCategory().getArticleCategories().remove(articleCategory);
                articleCategory.setArticle(null);
                articleCategory.setCategory(null);
            }
        }
    }
}