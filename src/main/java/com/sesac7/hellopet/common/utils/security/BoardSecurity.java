package com.sesac7.hellopet.common.utils.security;

import com.sesac7.hellopet.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("boardSecurity")
@RequiredArgsConstructor
public class BoardSecurity {
    private final BoardRepository boardRepository;

    public boolean isOwner(Long boardId, Authentication authentication) {
        return boardRepository.findById(boardId)
                              .map(board -> board.getUser().getEmail().equals(authentication.getName()))
                              .orElse(false);
    }
}
