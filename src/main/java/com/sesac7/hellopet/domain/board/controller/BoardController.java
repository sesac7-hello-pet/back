package com.sesac7.hellopet.domain.informalBoard.controller;

import com.sesac7.hellopet.domain.informalBoard.dto.board.request.BoardSearchRequest;
import com.sesac7.hellopet.domain.informalBoard.dto.board.response.BoardPageResponse;
import com.sesac7.hellopet.domain.informalBoard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        ResponseEntity.ok(boardService.getSearchList(request));
    }

}
