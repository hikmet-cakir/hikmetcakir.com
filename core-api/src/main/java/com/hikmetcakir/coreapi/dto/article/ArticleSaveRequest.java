package com.hikmetcakir.coreapi.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSaveRequest {

    private String title;
    private String content;
    private Integer topicId;
    private String createdBy;
}
