package com.hikmetcakir.coreapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikmetcakir.coreapi.dto.category.*;
import com.hikmetcakir.coreapi.entity.CategoryEntity;
import com.hikmetcakir.coreapi.repository.CategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDb() {
        repository.deleteAll();
    }

    private String createCategory(String name, String parentId) throws Exception {
        CategorySaveRequest req = CategorySaveRequest.builder()
                .name(name)
                .parentId(parentId)
                .build();

        String response = mvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, CategorySaveResponse.class).getId();
    }

    @Test
    void save_shouldCreateNewCategory() throws Exception {
        String id = createCategory("Electronics", null);

        CategoryEntity saved = repository.findById(id).orElseThrow();
        assertThat(saved.getName()).isEqualTo("Electronics");
        assertThat(saved.isDeleted()).isFalse();
    }

    @Test
    void update_shouldUpdateCategoryFields() throws Exception {
        String id = createCategory("Phone", null);

        CategoryUpdateRequest request = CategoryUpdateRequest.builder()
                .name("Smartphone")
                .build();

        mvc.perform(put("/category/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        CategoryEntity updated = repository.findById(id).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Smartphone");
    }

    @Test
    void query_shouldReturnAllCategories() throws Exception {
        createCategory("A", null);
        createCategory("B", null);

        mvc.perform(get("/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void queryCategoryHierarchy_shouldReturnCorrectLevels() throws Exception {
        String root = createCategory("Root", null);
        String child1 = createCategory("Child1", root);
        String child2 = createCategory("Child2", child1);

        mvc.perform(get("/category/hierarchy?levels=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Root"))
                .andExpect(jsonPath("$[0].children[0].name").value("Child1"))
                .andExpect(jsonPath("$[0].children[0].children[0].name").value("Child2"));
    }

    @Test
    void delete_shouldSoftDeleteRootAndChildren() throws Exception {
        String root = createCategory("Root", null);
        String c1 = createCategory("C1", root);
        String c2 = createCategory("C2", c1);

        mvc.perform(delete("/category/" + root))
                .andExpect(status().isOk());

        List<CategoryEntity> list = repository.findAll();

        assertThat(list)
                .allMatch(c -> c.isDeleted());
    }
}
