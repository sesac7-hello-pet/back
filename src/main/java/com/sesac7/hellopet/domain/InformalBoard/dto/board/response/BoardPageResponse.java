package com.sesac7.hellopet.domain.informalBoard.dto.board.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardPageListResponse {
    private int page;
    private int size;
    private int totalPages;
    private int totalCount;
    private List<BoardListResponse> boardList;
}
