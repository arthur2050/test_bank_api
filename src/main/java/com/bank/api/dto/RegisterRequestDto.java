package com.bank.api.dto;

import com.bank.api.entity.Role;
import lombok.Data;

@Data
public class RegisterRequestDto {
    private String username;
    private String password;
    private Role role;
}