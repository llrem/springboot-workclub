package com.yu.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskLogParam {
    private String name;
    private String object;
    private LocalDateTime createDate;
    private int type;
}
