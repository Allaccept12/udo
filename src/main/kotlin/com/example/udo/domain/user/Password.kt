package com.example.udo.domain.user

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Embeddable
data class Password(
    var value: String
) {
    init {
        value = BCryptPasswordEncoder().encode(value)
    }

    fun matches(plainText: String): Boolean {
        return BCryptPasswordEncoder().matches(plainText, value)
    }
}