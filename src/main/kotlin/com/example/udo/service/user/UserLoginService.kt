package com.example.udo.service.user

import com.example.udo.service.user.dto.LoginRequest
import com.example.udo.service.user.dto.LoginResponse

interface UserLoginService {
    fun login(request: LoginRequest): LoginResponse
}