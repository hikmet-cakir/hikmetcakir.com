package com.hikmetcakir.coreapi.dto.article;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUpdateRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String categoryId;
    @NotBlank
    private String updatedBy;
}
