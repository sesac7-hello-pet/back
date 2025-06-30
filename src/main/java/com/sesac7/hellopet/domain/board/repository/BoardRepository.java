package com.sesac7.hellopet.domain.board.repository;

import com.sesac7.hellopet.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("select  b from Board b where (:category = 'TOTAL' or str(b.boardCategory) = :category ) "
            + "and ("
            + "(:search = 'TOTAL' and (b.title like %:keyword% or b.content like %:keyword% or b.user.userDetail.nickname like %:keyword%))"
            + "or (:search = 'USERNAME' and (b.user.userDetail.nickname like %:keyword% )) "
            + " or (:search = 'TITLE' and (b.title like %:keyword%))"
            + "or (:search = 'CONTENT' and (b.content like %:keyword%))"
            + ")")
    Page<Board> search(@Param("category") String category, @Param("search") String searchType,
                       @Param("sort") String sort, @Param("keyword") String keyword, Pageable pageable);
}
