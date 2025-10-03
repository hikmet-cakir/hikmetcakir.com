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
@Document(collection = "user")
public class UserEntity {

    @Id
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String role;
}
