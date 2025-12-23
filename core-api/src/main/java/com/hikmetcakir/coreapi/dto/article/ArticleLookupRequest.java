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
public class ArticleLookupRequest {

    @NotBlank
    private String keyword;

    public String getNormalizedKeyword() {
        return keyword == null ? null : keyword.trim().toLowerCase();
    }
}
