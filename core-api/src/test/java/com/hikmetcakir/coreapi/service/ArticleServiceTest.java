package com.hikmetcakir.coreapi.service;

import com.hikmetcakir.coreapi.dto.article.ArticleQueryRequest;
import com.hikmetcakir.coreapi.dto.article.ArticleSummary;
import com.hikmetcakir.coreapi.entity.ArticleEntity;
import com.hikmetcakir.coreapi.respository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

        when(articleRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(mockEntity));
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

        when(articleRepository.findAll(Mockito.any(Example.class))).thenReturn(new ArrayList());
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

        when(articleRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(mockEntity));
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

        when(articleRepository.findAll(Mockito.any(Example.class))).thenReturn(List.of(mockEntity));
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
}
