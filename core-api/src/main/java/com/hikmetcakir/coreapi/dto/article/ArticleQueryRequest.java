package com.hikmetcakir.coreapi.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleQueryRequest {

    private String id;
    private String title;
    private String categoryId;
    private int size;
    private int page;
}
