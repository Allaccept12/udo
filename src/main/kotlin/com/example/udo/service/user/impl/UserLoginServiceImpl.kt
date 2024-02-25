package com.example.udo.service.user.impl

import com.example.udo.domain.user.UserRepository
import com.example.udo.service.authentication.JwtTokenService
import com.example.udo.service.user.UserLoginService
import com.example.udo.service.user.dto.LoginRequest
import com.example.udo.service.user.dto.LoginResponse
import org.springframework.stereotype.Service

@Service
class UserLoginServiceImpl(
    private val userRepository: UserRepository,
    private val jwtTokenService: JwtTokenService
): UserLoginService {

    override fun login(request: LoginRequest): LoginResponse {
        val userEntity = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Id or password is incorrect")
        userEntity.verifyPassword(request.password)

        return LoginResponse(jwtTokenService.createToken(userEntity.getId()))
    }
}