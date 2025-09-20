package com.hikmetcakir.coreapi.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSummary {

    private String id;
    private String title;
    private String content;
    private String createdBy;
    private OffsetDateTime created;
}
