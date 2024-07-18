package com.thread.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thread.sns.model.dto.ArticleDto;
import com.thread.sns.model.dto.ArticleRequest;
import com.thread.sns.model.Category;
import com.thread.sns.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category1;
    private Category category2;

    @BeforeEach 
    public void setUp() {
        categoryRepository.deleteAll();
        category1 = new Category();
        category1.setName("Category 1");
        categoryRepository.save(category1);

        category2 = new Category();
        category2.setName("Category 2");
        categoryRepository.save(category2);
    }

    @Test
    @Rollback(value = false)
    public void testCreateArticle() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        request.setCategoryIds(Arrays.asList(category1.getId(), category2.getId()));

        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.content").value("Test Content"))
                .andExpect(jsonPath("$.articleCategories").isArray());
    }

    @Test
    @Rollback(value = false)
    public void testUpdateArticle() throws Exception {
        // Create initial article
        ArticleRequest createRequest = new ArticleRequest();
        createRequest.setTitle("Initial Title");
        createRequest.setContent("Initial Content");
        createRequest.setCategoryIds(Arrays.asList(category1.getId(), category2.getId()));

        String response = mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ArticleDto createdArticle = objectMapper.readValue(response, ArticleDto.class);

        // Update article
        ArticleRequest updateRequest = new ArticleRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setContent("Updated Content");
        updateRequest.setCategoryIds(Collections.singletonList(category2.getId()));

        mockMvc.perform(put("/api/articles/" + createdArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdArticle.getId()))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.content").value("Updated Content"))
                .andExpect(jsonPath("$.articleCategories").isArray())
                .andExpect(jsonPath("$.articleCategories.length()").value(updateRequest.getCategoryIds().size()));
    }
}