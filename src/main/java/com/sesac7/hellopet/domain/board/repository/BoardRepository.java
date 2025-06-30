package com.sesac7.hellopet.domain.informalBoard.repository;

import com.sesac7.hellopet.domain.informalBoard.dto.board.request.BoardSearchRequest;
import com.sesac7.hellopet.domain.informalBoard.dto.board.response.BoardResponse;
import com.sesac7.hellopet.domain.informalBoard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<BoardResponse> search(BoardSearchRequest request);

}
