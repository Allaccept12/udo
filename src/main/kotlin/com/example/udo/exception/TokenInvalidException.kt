package com.example.udo.exception

import org.springframework.security.core.AuthenticationException

class TokenInvalidException(message: String? = null) : AuthenticationException(message) {
}