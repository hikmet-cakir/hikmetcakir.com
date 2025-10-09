package com.hikmetcakir.coreapi.dto.article;

import jakarta.validation.constraints.Min;
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
    @Min(1)
    private int size;
    @Min(0)
    private int page;
}
