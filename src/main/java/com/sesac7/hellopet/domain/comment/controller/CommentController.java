package com.sesac7.hellopet.domain.comment.controller;

import com.sesac7.hellopet.domain.comment.dto.request.BoardCommentCreateRequest;
import com.sesac7.hellopet.domain.comment.dto.response.BoardCommentResponse;
import com.sesac7.hellopet.domain.comment.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards/{boardId}/comments")
public class CommentController {
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<BoardCommentResponse> createComment(BoardCommentCreateRequest request,
                                                              @PathVariable Long boardId) {
        return ResponseEntity.ok(commentService.createComment(request, boardId));
    }
}
