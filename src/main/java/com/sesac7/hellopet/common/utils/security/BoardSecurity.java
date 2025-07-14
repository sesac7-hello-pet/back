package com.sesac7.hellopet.common.utils.security;

import com.sesac7.hellopet.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("boardSecurity")
@RequiredArgsConstructor
public class BoardSecurity {
    private final BoardRepository boardRepository;

    /**
     * 커스텀 어노테이션 사용을 위한 클래스입니다.
     * 위에 Component 어노테이션으로 이름을 지정해놨습니다.
     * isOwner 라는 메서드를 구현해서
     * boardId로 board를 찾고 찾은 엔티티의 작성자가 현재 로그인 된 작성자인지 확인합니다.
     *
     * @param boardId
     * @param authentication
     * @return
     */
    public boolean isOwner(Long boardId, Authentication authentication) {
        return boardRepository.findById(boardId)
                              .map(board -> board.getUser().getEmail().equals(authentication.getName()))
                              .orElse(false);
    }
}
