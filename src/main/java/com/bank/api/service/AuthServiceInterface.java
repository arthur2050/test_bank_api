package com.bank.api.service;

import com.bank.api.dto.AuthRequestDto;
import com.bank.api.dto.AuthResponseDto;
import com.bank.api.dto.RegisterRequestDto;

public interface AuthServiceInterface {
    AuthResponseDto register(RegisterRequestDto request);

    AuthResponseDto login(AuthRequestDto request);
}
