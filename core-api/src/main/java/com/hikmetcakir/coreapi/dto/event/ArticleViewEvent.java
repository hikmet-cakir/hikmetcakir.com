package com.hikmetcakir.coreapi.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleViewEvent {

    private String articleId;
    private String userId;
    private LocalDateTime timestamp;
}
