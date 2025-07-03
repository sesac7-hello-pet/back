package com.sesac7.hellopet.domain.comment.controller;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.comment.dto.request.CommentCreateRequest;
import com.sesac7.hellopet.domain.comment.dto.request.CommentUpdateRequest;
import com.sesac7.hellopet.domain.comment.dto.response.CommentPageResponse;
import com.sesac7.hellopet.domain.comment.dto.response.CommentResponse;
import com.sesac7.hellopet.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards/{boardId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentCreateRequest request,
                                                         @PathVariable Long boardId,
                                                         @AuthenticationPrincipal CustomUserDetails details) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(request, boardId, details));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<CommentPageResponse> getComments(@PathVariable Long boardId,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(commentService.getComments(boardId, page, size));
    }

    @PutMapping("{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long commentId, @RequestBody
    CommentUpdateRequest request, @AuthenticationPrincipal CustomUserDetails details) {
        return ResponseEntity.ok(commentService.updateComment(commentId, request, details));
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @AuthenticationPrincipal CustomUserDetails details) {
        commentService.deleteComment(commentId, details);
        return ResponseEntity.noContent().build();
    }

}
