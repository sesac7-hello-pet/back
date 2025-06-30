package com.sesac7.hellopet.domain.informalBoard.service;

import com.sesac7.hellopet.domain.informalBoard.dto.board.request.BoardSearchRequest;
import com.sesac7.hellopet.domain.informalBoard.dto.board.response.BoardPageResponse;
import com.sesac7.hellopet.domain.informalBoard.dto.board.response.BoardResponse;
import com.sesac7.hellopet.domain.informalBoard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository repository;

    public BoardPageResponse getSearchList(BoardSearchRequest request) {
        Page<BoardResponse> page = repository.search(request);
        return BoardPageResponse.from(page, request);
    }
}
