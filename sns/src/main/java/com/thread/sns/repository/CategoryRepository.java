package com.thread.sns.repository;

import com.thread.sns.model.Article;
import com.thread.sns.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
