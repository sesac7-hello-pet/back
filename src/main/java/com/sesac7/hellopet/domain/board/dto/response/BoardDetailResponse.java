package com.sesac7.hellopet.domain.board.dto.response;

import com.sesac7.hellopet.domain.board.entity.Board;
import com.sesac7.hellopet.domain.comment.dto.response.CommentResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardDetailResponse {
    private BoardResponse boardResponse;
    private List<CommentResponse> commentList = new ArrayList<>();

    public static BoardDetailResponse from(Board board) {
        return new BoardDetailResponse(
                BoardResponse.from(board),
                board.getComment().stream().map(CommentResponse::from).toList()
        );

    }
}
