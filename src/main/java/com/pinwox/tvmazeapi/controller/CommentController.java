package com.pinwox.tvmazeapi.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pinwox.tvmazeapi.model.dto.CommentRequestDTO;
import com.pinwox.tvmazeapi.service.CommentService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public ResponseEntity<Map<String, Object>> createComment(@Valid @RequestBody CommentRequestDTO request) {
        commentService.saveComment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "status", HttpStatus.CREATED.value(),
                        "message", "Comentario guardado correctamente"
                ));
    }

}
