package com.sesac7.hellopet.domain.comment.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.board.entity.Board;
import com.sesac7.hellopet.domain.board.repository.BoardRepository;
import com.sesac7.hellopet.domain.comment.dto.request.CommentCreateRequest;
import com.sesac7.hellopet.domain.comment.dto.request.CommentUpdateRequest;
import com.sesac7.hellopet.domain.comment.dto.response.CommentPageResponse;
import com.sesac7.hellopet.domain.comment.dto.response.CommentResponse;
import com.sesac7.hellopet.domain.comment.entity.Comment;
import com.sesac7.hellopet.domain.comment.repository.CommentRepository;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.service.UserFinder;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

        Comment comment = request.toDomain(board, user);

        Comment saved = commentRepository.save(comment);

        board.setCommentsCount(board.getCommentsCount() + 1);
        return CommentResponse.from(saved);

    }

    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request, CustomUserDetails details) {
        Comment comment = commentRepository.findById(commentId)
                                           .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        String writer = comment.getUser().getEmail();
        if (writer.equals(details.getUsername())) {
            comment.setContent(request.getContent());
            comment.setUpdatedAt(LocalDateTime.now());
            return CommentResponse.from(comment);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 댓글을 수정할 권한이 없습니다.");
        }

    }

    public void deleteComment(Long commentId, CustomUserDetails details) {
        Comment comment = commentRepository.findById(commentId)
                                           .orElseThrow(() -> new EntityNotFoundException("댓글이 존재하지 않습니다."));

        String writer = comment.getUser().getEmail();
        if (writer.equals(details.getUsername())) {
            commentRepository.delete(comment);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 댓글을 삭제할 권한이 없습니다.");
        }

    }

    public CommentPageResponse getMyComments(String email, int page, int size) {
        Page<Comment> commentPage = commentRepository.findByUserEmail(email,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
        Page<CommentResponse> pageResponse = commentPage.map(CommentResponse::from);
        return CommentPageResponse.from(pageResponse, page, size);

    }
}
