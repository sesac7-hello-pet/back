package com.sesac7.hellopet.domain.comment.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.board.entity.Board;
import com.sesac7.hellopet.domain.board.repository.BoardRepository;
import com.sesac7.hellopet.domain.comment.dto.request.CommentCreateRequest;
import com.sesac7.hellopet.domain.comment.dto.response.CommentResponse;
import com.sesac7.hellopet.domain.comment.entity.Comment;
import com.sesac7.hellopet.domain.comment.repository.CommentRepository;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.service.UserFinder;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserFinder userFinder;

    public CommentResponse createComment(CommentCreateRequest request, Long boardId,
                                         CustomUserDetails details) {

        // 보드 게시글 
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("해당 게시글이 없습니다."));
        // 유저 정보
        User user = userFinder.findLoggedInUserByUsername(details.getUsername());

        Comment comment = Comment.builder()
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .board(board)
                .user(user)
                .build();

        Comment saved = commentRepository.save(comment);
        return CommentResponse.from(saved);

    }
}
