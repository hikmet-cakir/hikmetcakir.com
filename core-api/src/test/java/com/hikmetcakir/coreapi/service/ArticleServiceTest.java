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

import java.util.ArrayList;
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
    void query_givenArticleIdAndExistOneArticleInDB_returnOneRecordAsList() {
        // region Given
        String articleId = UUID.randomUUID().toString();
        ArticleQueryRequest request = ArticleQueryRequest.builder()
                .id(articleId)
                .build();

        ArticleEntity mockEntity = ArticleEntity.builder()
                .id(articleId)
                .build();

        when(articleRepository.findAll(any(Example.class))).thenReturn(List.of(mockEntity));
        // endregion

        // region When
        List<ArticleSummary> result = articleService.query(request);
        // endregion

        // region Then
        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .first()
                .extracting(ArticleSummary::getId)
                .isEqualTo(articleId);
        // endregion
    }

    @Test
    void query_givenArticleIdAndNotExistArticleInDB_returnOneRecordAsList() {
        // region Given
        String articleId = UUID.randomUUID().toString();
        ArticleQueryRequest request = ArticleQueryRequest.builder()
                .id(articleId)
                .build();

        ArticleEntity mockEntity = ArticleEntity.builder()
                .id(articleId)
                .build();

        when(articleRepository.findAll(any(Example.class))).thenReturn(new ArrayList());
        // endregion

        // region When
        List<ArticleSummary> result = articleService.query(request);
        // endregion

        // region Then
        assertThat(result)
                .isNotNull()
                .hasSize(0);
        // endregion
    }

    @Test
    void query_givenArticleTitleAndExistOneArticleInDB_returnOneRecordAsList() {
        // region Given
        String articleTitle = "DummyTitle";
        ArticleQueryRequest request = ArticleQueryRequest.builder()
                .id(articleTitle)
                .build();

        ArticleEntity mockEntity = ArticleEntity.builder()
                .title(articleTitle)
                .build();

        when(articleRepository.findAll(any(Example.class))).thenReturn(List.of(mockEntity));
        // endregion

        // region When
        List<ArticleSummary> result = articleService.query(request);
        // endregion

        // region Then
        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .first()
                .extracting(ArticleSummary::getTitle)
                .isEqualTo(articleTitle);
        // endregion
    }

    @Test
    void query_givenArticleTopicIdAndExistOneArticleInDB_returnOneRecordAsList() {
        // region Given
        Integer articleTopicId = 1;
        ArticleQueryRequest request = ArticleQueryRequest.builder()
                .topicId(articleTopicId)
                .build();

        ArticleEntity mockEntity = ArticleEntity.builder()
                .topicId(articleTopicId)
                .build();

        when(articleRepository.findAll(any(Example.class))).thenReturn(List.of(mockEntity));
        // endregion

        // region When
        List<ArticleSummary> result = articleService.query(request);
        // endregion

        // region Then
        assertThat(result)
                .isNotNull()
                .hasSize(1);
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
