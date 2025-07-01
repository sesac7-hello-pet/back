package com.sesac7.hellopet.domain.comment.service;

import com.sesac7.hellopet.domain.comment.dto.request.BoardCommentCreateRequest;
import com.sesac7.hellopet.domain.comment.dto.response.BoardCommentResponse;
import com.sesac7.hellopet.domain.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    public BoardCommentResponse createComment(BoardCommentCreateRequest request, Long boardId) {
        
    }
}
