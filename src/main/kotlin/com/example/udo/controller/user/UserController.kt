package com.example.udo.controller.user

import com.example.udo.service.user.UserLoginService
import com.example.udo.service.user.UserRegisterService
import com.example.udo.service.user.dto.LoginRequest
import com.example.udo.service.user.dto.LoginResponse
import com.example.udo.service.user.dto.RegisterRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class UserController(
    private val userRegisterService: UserRegisterService,
    private val userLoginService: UserLoginService
) {

    @PostMapping("/user/register")
    fun register(@Valid @RequestBody request: RegisterRequest) {
        userRegisterService.register(request)
    }

    @PostMapping("/user/login")
    fun login(@Valid @RequestBody request: LoginRequest): LoginResponse {
        return userLoginService.login(request)
    }
}