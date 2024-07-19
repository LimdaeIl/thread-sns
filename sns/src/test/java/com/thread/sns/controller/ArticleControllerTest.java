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
    private Category category3;
    private Category category4;

    @BeforeEach
    public void setUp() {
        category1 = new Category();
        category1.setName("자바");
        categoryRepository.save(category1);

        category2 = new Category();
        category2.setName("스프링");
        categoryRepository.save(category2);

        category3 = new Category();
        category3.setName("스프링부트");
        categoryRepository.save(category3);


        category4 = new Category();
        category4.setName("프로그래밍 언어");
        categoryRepository.save(category4);
    }

    @Test
    @Rollback(value = false)
    public void testCreateArticle() throws Exception {
        ArticleRequest request = new ArticleRequest();
        request.setTitle("게시글 1");
        request.setContent("게시글 1 내용");
        request.setCategoryIds(Arrays.asList(category1.getId(), category2.getId(), category3.getId()));

        mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("게시글 1"))
                .andExpect(jsonPath("$.content").value("게시글 1 내용"))
                .andExpect(jsonPath("$.articleCategories").isArray());
    }

    @Test
    @Rollback(value = false)
    public void testUpdateArticle() throws Exception {
        // Create initial article
        ArticleRequest createRequest = new ArticleRequest();
        createRequest.setTitle("게시글 1");
        createRequest.setContent("게시글 1 내용");
        createRequest.setCategoryIds(Arrays.asList(category1.getId(), category2.getId(), category3.getId()));

        String response = mockMvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ArticleDto createdArticle = objectMapper.readValue(response, ArticleDto.class);

        // Update article
        ArticleRequest updateRequest = new ArticleRequest();
        updateRequest.setTitle("수정된 게시글 1");
        updateRequest.setContent("수정된 게시글 1 내용");
        updateRequest.setCategoryIds(Collections.singletonList(category3.getId()));

        mockMvc.perform(put("/api/articles/" + createdArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdArticle.getId()))
                .andExpect(jsonPath("$.title").value("수정된 게시글 1"))
                .andExpect(jsonPath("$.content").value("수정된 게시글 1 내용"))
                .andExpect(jsonPath("$.articleCategories").isArray())
                .andExpect(jsonPath("$.articleCategories.length()").value(updateRequest.getCategoryIds().size()));
    }
}