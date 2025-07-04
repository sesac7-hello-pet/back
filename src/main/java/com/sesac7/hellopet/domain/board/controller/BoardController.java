package com.sesac7.hellopet.domain.board.controller;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.board.dto.request.BoardCreateRequest;
import com.sesac7.hellopet.domain.board.dto.request.BoardSearchRequest;
import com.sesac7.hellopet.domain.board.dto.request.BoardUpdateRequest;
import com.sesac7.hellopet.domain.board.dto.response.BoardDetailResponse;
import com.sesac7.hellopet.domain.board.dto.response.BoardPageResponse;
import com.sesac7.hellopet.domain.board.dto.response.BoardResponse;
import com.sesac7.hellopet.domain.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<BoardPageResponse> getSearchList(BoardSearchRequest request) {
        return ResponseEntity.ok(boardService.getSearchList(request));
    }

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@RequestBody @Valid BoardCreateRequest request,
                                                     @AuthenticationPrincipal CustomUserDetails details) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.createBoard(request, details));
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long boardId,
                                                     @RequestBody @Valid BoardUpdateRequest request) {
        return ResponseEntity.ok(boardService.updateBoard(boardId, request));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId,
                                            @AuthenticationPrincipal CustomUserDetails details) {
        boardService.deleteBoard(boardId, details);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetailResponse> getBoardDetail(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoardDetail(boardId));

    }

    @PutMapping("/{boardId}/like")
    public ResponseEntity<Void> updateLike(@PathVariable Long boardId) {
        boardService.updateLike(boardId);
        return ResponseEntity.noContent().build();
    }

}
