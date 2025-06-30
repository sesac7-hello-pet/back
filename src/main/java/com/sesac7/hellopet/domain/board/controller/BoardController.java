package com.sesac7.hellopet.domain.board.controller;

import com.sesac7.hellopet.domain.board.dto.request.BoardSearchRequest;
import com.sesac7.hellopet.domain.board.dto.response.BoardPageResponse;
import com.sesac7.hellopet.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<BoardPageResponse> getSearchList(BoardSearchRequest request) {
        return ResponseEntity.ok(boardService.getSearchList(request));
    }

}
