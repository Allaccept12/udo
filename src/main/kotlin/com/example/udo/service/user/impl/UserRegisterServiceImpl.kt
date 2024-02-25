package com.example.udo.service.user.impl

import com.example.udo.domain.user.UserEntity
import com.example.udo.domain.user.UserRepository
import com.example.udo.service.user.UserRegisterService
import com.example.udo.service.user.dto.RegisterRequest
import com.example.udo.service.user.validator.UserRegisterValidator
import org.springframework.stereotype.Service

@Service
class UserRegisterServiceImpl(
    private val userRepository: UserRepository,
    private val userRegisterValidator: UserRegisterValidator
): UserRegisterService {

    override fun register(request: RegisterRequest) {
        userRegisterValidator.validateExistingEmail(request.email)
        UserEntity.create(request.name, request.email, request.phoneNumber, request.password)
            .let(userRepository::save)
    }
}