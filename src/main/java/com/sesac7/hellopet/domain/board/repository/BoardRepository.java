package com.sesac7.hellopet.domain.board.repository;

import com.sesac7.hellopet.domain.board.entity.InformalBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<InformalBoard, Long> {
}
