package com.sesac7.hellopet.domain.board.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.board.dto.request.BoardCreateRequest;
import com.sesac7.hellopet.domain.board.dto.request.BoardSearchRequest;
import com.sesac7.hellopet.domain.board.dto.request.BoardUpdateRequest;
import com.sesac7.hellopet.domain.board.dto.request.SortType;
import com.sesac7.hellopet.domain.board.dto.response.BoardDetailResponse;
import com.sesac7.hellopet.domain.board.dto.response.BoardPageResponse;
import com.sesac7.hellopet.domain.board.dto.response.BoardResponse;
import com.sesac7.hellopet.domain.board.entity.Board;
import com.sesac7.hellopet.domain.board.repository.BoardRepository;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.service.UserFinder;
import com.sesac7.hellopet.global.annotation.IsBoardOwner;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository repository;
    private final UserFinder userFinder;

    @Transactional(readOnly = true)
    public BoardPageResponse getSearchList(BoardSearchRequest request) {
        Sort sort = getSortBySortType(request.getSortType());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<BoardResponse> responses = repository.search(request.getCategory().name(), request.getSearchType().name(),
                request.getSortType().name(), request.getKeyword(), pageable).map(BoardResponse::from);

        return BoardPageResponse.from(responses, request);
    }

    private Sort getSortBySortType(SortType sortType) {
        switch (sortType) {
            case CURRENT:
                return Sort.by(Sort.Direction.DESC, "createdAt");
            case LIKES:
                return Sort.by(Sort.Direction.DESC, "likesCount");
            case COMMENTS:
                return Sort.by(Sort.Direction.DESC, "commentsCount");
            case VIEWS:
                return Sort.by(Sort.Direction.DESC, "viewsCount");
            default:
                return Sort.by(Sort.Direction.DESC, "createdAt");
        }

    }

    public BoardResponse createBoard(BoardCreateRequest request, CustomUserDetails details) {
        User user = userFinder.findLoggedInUserByUsername(details.getUsername());
        Board board = BoardCreateRequest.from(request, user);
        Board saved = repository.save(board);
        return BoardResponse.from(saved);
    }

    @IsBoardOwner
    public BoardResponse updateBoard(Long boardId, BoardUpdateRequest request) {
        Board board = repository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));
        board.setTitle(request.getTitle());
        board.setContent(request.getContent());
        board.setImage_url(request.getImg_url());
        board.setBoardCategory(request.getBoardCategory());
        board.setPetType(request.getPetType());
        board.setUpdatedAt(LocalDateTime.now());
        return BoardResponse.from(board);
    }

    public void deleteBoard(Long boardId, CustomUserDetails details) {
        Board board = repository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));
        String writer = board.getUser().getEmail();
        String userEmail = details.getUsername();
        if (writer.equals(userEmail)) {
            repository.delete(board);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 게시글을 삭제할 권한이 없습니다.");
        }
    }

    public BoardDetailResponse getBoardDetail(Long boardId) {
        Board board = repository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));
        board.setViewsCount(board.getViewsCount() + 1);
        return BoardDetailResponse.from(board);
    }

    public BoardPageResponse getMyBoard(CustomUserDetails details, int page, int size) {
        User user = userFinder.findLoggedInUserByUsername(details.getUsername());
        BoardSearchRequest request = BoardSearchRequest.toRequest(user.getEmail(), page, size);
        BoardPageResponse response = getSearchList(request);
        return response;
    }

    public void updateLike(Long boardId) {
        Board board = repository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));
        board.setLikesCount(board.getLikesCount() + 1);
    }

}
