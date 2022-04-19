package com.mongo.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY,
getterVisibility = JsonAutoDetect.Visibility.NONE,
isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@Document
public class User {
    @Id
    private Integer id;
    @Field("name")
    private String name;
    @Field("salary")
    private Double salary;
    @Field("birthday")
    private Date birthday;
}
