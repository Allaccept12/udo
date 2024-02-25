package com.example.udo.service.user

import com.example.udo.service.user.dto.RegisterRequest

interface UserRegisterService {
    fun register(request: RegisterRequest)
}