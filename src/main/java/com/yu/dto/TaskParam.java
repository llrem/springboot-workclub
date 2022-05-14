package com.yu.dto;

import lombok.Data;

@Data
public class TaskParam {
    private Long id;
    private String description;
    private int status;
}
