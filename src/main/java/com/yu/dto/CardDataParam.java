package com.yu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDataParam {
    String name;
    int value;
    int percentage;
    String color;
}
