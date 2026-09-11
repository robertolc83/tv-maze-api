package com.pinwox.tvmazeapi.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.pinwox.tvmazeapi.model.Comment;
import com.pinwox.tvmazeapi.model.dto.CommentRequestDTO;
import com.pinwox.tvmazeapi.repository.CommentRepository;

@Service 
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(CommentRequestDTO request) {
        Comment comment = new Comment(
                null,
                request.getShowId(),
                request.getComment(),
                request.getRating(),
                Instant.now()
        );
        return commentRepository.save(comment);
    }

}
