package com.sesac7.hellopet.domain.comment.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class CommentPageResponse {
    private int page;
    private int size;
    private int totalPage;
    private Long totalSize;
    private List<CommentResponse> commentList = new ArrayList<>();

    public static CommentPageResponse from(Page<CommentResponse> response, int page, int size) {
        return new CommentPageResponse(
                page, size, response.getTotalPages(), response.getTotalElements(),
                response.getContent()
        );

    }
}
