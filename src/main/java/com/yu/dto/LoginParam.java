package com.yu.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginParam {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
