package com.yu.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChangeDateParam {
    Long id;
    LocalDateTime date;
}
