package com.hikmetcakir.coreapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikmetcakir.coreapi.dto.article.*;
import com.hikmetcakir.coreapi.entity.ArticleEntity;
import com.hikmetcakir.coreapi.respository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ArticleControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ArticleRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }

    private String createArticle(String title, String content, String categoryId, String createdBy) throws Exception {
        ArticleSaveRequest req = ArticleSaveRequest.builder()
                .title(title)
                .content(content)
                .categoryId(categoryId)
                .createdBy(createdBy)
                .build();

        String response = mvc.perform(post("/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, ArticleSaveResponse.class).getId();
    }

    @Test
    void save_shouldCreateNewArticle() throws Exception {
        String id = createArticle("Test Article", "This is content", "cat123", "user1");

        ArticleEntity saved = repository.findById(id).orElseThrow();
        assertThat(saved.getTitle()).isEqualTo("Test Article");
        assertThat(saved.getContent()).isEqualTo("This is content");
        assertThat(saved.getCategoryId()).isEqualTo("cat123");
        assertThat(saved.getCreatedBy()).isEqualTo("user1");
        assertThat(saved.isDeleted()).isFalse();
    }

    @Test
    void update_shouldModifyArticle() throws Exception {
        String id = createArticle("Old Title", "Old content", "cat123", "user1");


        ArticleUpdateRequest updateRequest = ArticleUpdateRequest.builder()
                .title("New Title")
                .content("New content")
                .categoryId("New Category Id")
                .updatedBy("49001")
                .build();

        mvc.perform(put("/article/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        ArticleEntity updated = repository.findById(id).orElseThrow();
        assertThat(updated.getTitle()).isEqualTo("New Title");
        assertThat(updated.getContent()).isEqualTo("New content");
    }

    @Test
    void delete_shouldSoftDeleteArticle() throws Exception {
        String id = createArticle("Title", "Content", "cat123", "user1");

        mvc.perform(delete("/article/" + id))
                .andExpect(status().isOk());

        ArticleEntity deleted = repository.findById(id).orElseThrow();
        assertThat(deleted.isDeleted()).isTrue();
    }

    @Test
    void query_shouldReturnArticlesMatchingCriteria() throws Exception {
        // Oluştur bazı makaleler
        createArticle("Java Spring", "Content A", "cat1", "user1");
        createArticle("Spring Boot", "Content B", "cat1", "user2");
        createArticle("Python Guide", "Content C", "cat2", "user3");

        // Sorgu isteği oluştur (title içeren spring)
        ArticleQueryRequest queryRequest = ArticleQueryRequest.builder()
                .title("spring")
                .page(0)
                .size(10)
                .build();

        String queryJson = objectMapper.writeValueAsString(queryRequest);

        mvc.perform(get("/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "spring") // ModelAttribute ile geliyor
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleSummaryList.length()").value(2))
                .andExpect(jsonPath("$.articleSummaryList[0].title").exists())
                .andExpect(jsonPath("$.articleSummaryList[1].title").exists());
    }

    @Test
    void query_byId_shouldReturnSingleArticle() throws Exception {
        String id = createArticle("Unique Title", "Unique Content", "cat1", "user1");

        mvc.perform(get("/article")
                        .param("id", id)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleSummaryList.length()").value(1))
                .andExpect(jsonPath("$.articleSummaryList[0].id").value(id));
    }
}
