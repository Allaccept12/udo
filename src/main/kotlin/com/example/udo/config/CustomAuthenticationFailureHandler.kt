package com.example.udo.config

import com.example.udo.exception.dto.FailDto
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler


class CustomAuthenticationFailureHandler(private val objectMapper: ObjectMapper) : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        val result = FailDto(
            code = HttpStatus.UNAUTHORIZED.value(),
            message = "사용할 수 없는 토큰입니다."
        )
        response.writer.write(objectMapper.writeValueAsString(result))
    }
}
