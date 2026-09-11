package com.pinwox.tvmazeapi.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Document (collection = "comments")
public class Comment {

    @Id //Mongo le asigna automáticamente un ObjectId único al guardar
    private String id;

    private Long showId;
    private String comment;
    private Integer rating;
    private Instant createdAt;

}
