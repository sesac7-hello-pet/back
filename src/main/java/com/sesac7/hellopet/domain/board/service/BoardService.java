package com.sesac7.hellopet.domain.board.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.board.dto.request.BoardCreateRequest;
import com.sesac7.hellopet.domain.board.dto.request.BoardSearchRequest;
import com.sesac7.hellopet.domain.board.dto.request.SortType;
import com.sesac7.hellopet.domain.board.dto.response.BoardPageResponse;
import com.sesac7.hellopet.domain.board.dto.response.BoardResponse;
import com.sesac7.hellopet.domain.board.entity.Board;
import com.sesac7.hellopet.domain.board.repository.BoardRepository;
import com.sesac7.hellopet.domain.user.Service.UserFinder;
import com.sesac7.hellopet.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository repository;
    private final UserFinder userFinder;

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

    public BoardResponse createBoard(BoardCreateRequest request, CustomUserDetails customUserDetails) {
        User user = userFinder.findLoginedUserByUsername(customUserDetails.getUsername());
        Board board = BoardCreateRequest.from(request, user);
        Board saved = repository.save(board);
        return BoardResponse.from(saved);
    }
}
