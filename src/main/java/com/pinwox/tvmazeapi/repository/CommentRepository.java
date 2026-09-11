package com.pinwox.tvmazeapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pinwox.tvmazeapi.model.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByShowId(Long showId);

}
