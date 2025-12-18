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
public class CategoryViewEvent {

    private String categoryId;
    private LocalDateTime timestamp;
}
