package com.example.udo.service.user.validator

import com.example.udo.domain.user.UserRepository
import org.springframework.stereotype.Component


@Component
class UserRegisterValidator(
    private val userRepository: UserRepository
) {

    fun validateExistingEmail(email: String) {
        if(userRepository.existsByEmail(email)) {
            throw IllegalArgumentException("Email already exists")
        }
    }

}