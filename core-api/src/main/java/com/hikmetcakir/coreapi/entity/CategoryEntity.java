package com.hikmetcakir.coreapi.entity;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Document(collection = "category")
public class CategoryEntity {

    @Id
    private String id;
    private String name;
    private String parentId;
    private String type;


}
