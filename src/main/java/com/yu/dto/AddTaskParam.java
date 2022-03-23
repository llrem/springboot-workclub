package com.yu.dto;

import lombok.Data;

@Data
public class AddTaskParam {
    private Long boardId;
    private String description;
}
