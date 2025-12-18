package com.hikmetcakir.analyticsapi.entity;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Document(collection = "category_views")
public class CategoryViewEntity {

    @Id
    private String id;
    private String categoryId;
    private LocalDateTime timestamp;
}
