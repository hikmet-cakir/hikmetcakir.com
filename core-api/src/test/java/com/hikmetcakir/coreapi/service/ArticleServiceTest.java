package com.hikmetcakir.coreapi.service;

import com.hikmetcakir.coreapi.dto.article.ArticleQueryRequest;
import com.hikmetcakir.coreapi.dto.article.ArticleSaveRequest;
import com.hikmetcakir.coreapi.dto.article.ArticleSummary;
import com.hikmetcakir.coreapi.dto.article.ArticleUpdateRequest;
import com.hikmetcakir.coreapi.entity.ArticleEntity;
import com.hikmetcakir.coreapi.respository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    @InjectMocks
    ArticleService articleService;

    @Mock
    ArticleRepository articleRepository;

    @Test
    void query_givenArticleTitleMatches_returnOneRecordAsList() {
        // region Given
        ArticleEntity article = ArticleEntity.builder()
                .id("1")
                .title("Spring Boot")
                .topicId(1)
                .build();

        ArticleQueryRequest request = new ArticleQueryRequest();
        request.setTitle("spring");
        request.setPage(0);
        request.setSize(10);

        Page<ArticleEntity> page = new PageImpl<>(List.of(article));
        when(articleRepository.findAll(any(Example.class), any(PageRequest.class))).thenReturn(page);
        // endregion

        // region When
        List<ArticleSummary> result = articleService.query(request);
        // endregion

        // region Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains("Spring Boot");
        // endregion
    }

    @Test
    void query_givenTopicIdMatchesTwoArticles_returnTwoRecordsAsList() {
        // region Given
        ArticleEntity article1 = ArticleEntity.builder().id("1").title("Spring Boot").topicId(1).build();
        ArticleEntity article2 = ArticleEntity.builder().id("2").title("Java Concurrency").topicId(1).build();

        ArticleQueryRequest request = new ArticleQueryRequest();
        request.setTopicId(1);
        request.setPage(0);
        request.setSize(10);

        Page<ArticleEntity> page = new PageImpl<>(List.of(article1, article2));
        when(articleRepository.findAll(any(Example.class), any(PageRequest.class))).thenReturn(page);
        // endregion

        // region When
        List<ArticleSummary> result = articleService.query(request);
        // endregion

        // region Then
        assertThat(result).hasSize(2);
        // endregion
    }

    @Test
    void query_givenNoArticleMatches_returnEmptyList() {
        // region Given
        ArticleQueryRequest request = new ArticleQueryRequest();
        request.setTitle("Data Structures & Algorithms");
        request.setPage(0);
        request.setSize(10);

        Page<ArticleEntity> page = new PageImpl<>(List.of());
        when(articleRepository.findAll(any(Example.class), any(PageRequest.class))).thenReturn(page);
        // endregion

        // region When
        List<ArticleSummary> result = articleService.query(request);
        // endregion

        // region Then
        assertThat(result).isEmpty();
        // endregion
    }

    @Test
    void query_givenPaginationSizeOneAndTwoArticlesInDB_returnOneRecordAsList() {
        // region Given
        ArticleEntity article1 = ArticleEntity.builder()
                .id("1").title("Spring Boot").topicId(1).build();

        ArticleQueryRequest request = new ArticleQueryRequest();
        request.setPage(0);
        request.setSize(1);

        Page<ArticleEntity> page = new PageImpl<>(List.of(article1), PageRequest.of(0, 1), 2);
        when(articleRepository.findAll(any(Example.class), any(PageRequest.class))).thenReturn(page);
        // endregion

        // region When
        List<ArticleSummary> result = articleService.query(request);
        // endregion

        // region Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("1");
        // endregion
    }

    @Test
    void save_givenFullFilledArticle_saveAndReturnGeneratedID() {
        // region Given
        ArticleSaveRequest request = ArticleSaveRequest.builder()
                .title("DummyTitle")
                .content("DummyContent")
                .topicId(1)
                .createdBy("75872")
                .build();

        ArticleEntity mockEntity = ArticleEntity.builder()
                .id(UUID.randomUUID().toString())
                .build();
        when(articleRepository.save(any())).thenReturn(mockEntity);
        // endregion

        // region When
        var article = articleService.save(request);
        // endregion

        // region Then
        assertThat(article)
                .isNotNull()
                .isNotBlank();
        // endregion
    }

    @Test
    void save_givenRepositoryThrowsException_thenPropagateException() {
        // region Given
        ArticleSaveRequest request = ArticleSaveRequest.builder()
                .title("DummyTitle")
                .content("DummyContent")
                .topicId(1)
                .createdBy("75872")
                .build();

        when(articleRepository.save(any())).thenThrow(new RuntimeException("DB error"));
        // endregion

        // region When & Then
        assertThatThrownBy(() -> articleService.save(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB error");
        // endregion
    }


    @Test
    void update_givenValidIdAndRequest_updatesEntity() {
        // region Given
        String id = UUID.randomUUID().toString();

        ArticleUpdateRequest request = ArticleUpdateRequest.builder()
                .title("UpdatedTitle")
                .content("UpdatedContent")
                .build();

        ArticleEntity existingEntity = ArticleEntity.builder()
                .id(id)
                .title("OldTitle")
                .content("OldContent")
                .build();

        when(articleRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(articleRepository.save(any())).thenReturn(existingEntity);
        // endregion

        // region When
        articleService.update(id, request);
        // endregion

        // region Then
        assertThat(existingEntity.getTitle()).isEqualTo("UpdatedTitle");
        assertThat(existingEntity.getContent()).isEqualTo("UpdatedContent");

        verify(articleRepository).findById(id);
        verify(articleRepository).save(existingEntity);
        // endregion
    }

    @Test
    void update_givenInvalidId_throwsException() {
        // region Given
        String id = UUID.randomUUID().toString();
        ArticleUpdateRequest request = ArticleUpdateRequest.builder().title("Updated").build();

        when(articleRepository.findById(id)).thenReturn(Optional.empty());
        // endregion

        // region Then
        assertThatThrownBy(() -> articleService.update(id, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("cannot be updated");

        verify(articleRepository).findById(id);
        verify(articleRepository, never()).save(any());
        // endregion
    }

    @Test
    void delete_givenValidId_marksEntityAsDeleted() {
        // region Given
        String id = UUID.randomUUID().toString();

        ArticleEntity existingEntity = ArticleEntity.builder()
                .id(id)
                .deleted(false)
                .build();

        when(articleRepository.findById(id)).thenReturn(Optional.of(existingEntity));
        when(articleRepository.save(any())).thenReturn(existingEntity);
        // endregion

        // region When
        articleService.delete(id);
        // endregion

        // region Then
        assertThat(existingEntity.isDeleted()).isTrue();

        verify(articleRepository).findById(id);
        verify(articleRepository).save(existingEntity);
        // endregion
    }

    @Test
    void delete_givenInvalidId_throwsException() {
        // region Given
        String id = UUID.randomUUID().toString();
        when(articleRepository.findById(id)).thenReturn(Optional.empty());
        // endregion

        // region Then
        assertThatThrownBy(() -> articleService.delete(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("cannot be deleted");

        verify(articleRepository).findById(id);
        verify(articleRepository, never()).save(any());
        // endregion
    }
}
