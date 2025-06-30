package com.sesac7.hellopet.domain.board.service;

import com.sesac7.hellopet.domain.board.dto.request.BoardSearchRequest;
import com.sesac7.hellopet.domain.board.dto.response.BoardPageResponse;
import com.sesac7.hellopet.domain.board.dto.response.BoardResponse;
import com.sesac7.hellopet.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository repository;

    public BoardPageResponse getSearchList(BoardSearchRequest request) {

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<BoardResponse> responses = repository.search(request.getCategory(), request.getSearchType(),
                request.getKeyword(), pageable);

        return BoardPageResponse.from(responses, request);
    }
}
