package com.yu.dto;

import lombok.Data;

import java.util.List;

@Data
public class LineDataParam {
    List<String> dates;
    List<Integer> numbers;
}
