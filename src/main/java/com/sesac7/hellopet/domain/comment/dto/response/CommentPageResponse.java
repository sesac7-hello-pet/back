package com.sesac7.hellopet.domain.comment.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class CommentPageResponse {
    private int page;
    private int size;
    private int totalPage;
    private int totalSize;
    private List<CommentResponse> commentList = new ArrayList<>();
    
}
