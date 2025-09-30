package com.hikmetcakir.coreapi.entity;

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
@Document(collection = "article")
public class ArticleEntity {

    @Id
    private String id;
    private String title;
    private String content;
    private String categoryId;
    private String createdBy;
    private LocalDateTime created;
    private String updatedBy;
    private LocalDateTime updated;
    private boolean deleted;
}
