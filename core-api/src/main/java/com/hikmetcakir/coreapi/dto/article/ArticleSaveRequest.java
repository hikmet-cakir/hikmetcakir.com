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
    private String categoryId;
    private String createdBy;
}
