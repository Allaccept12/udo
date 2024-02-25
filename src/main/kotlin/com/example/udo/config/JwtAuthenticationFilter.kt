package com.example.udo.config

import com.example.udo.exception.TokenInvalidException
import com.example.udo.service.authentication.JwtTokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtTokenService: JwtTokenService,
    private val authenticationFailureHandler: CustomAuthenticationFailureHandler
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val bearerToken = request.getHeader("Authorization")
        val bearerPrefix = "Bearer "

        try {
            if (bearerToken != null && bearerToken.startsWith(bearerPrefix)) {
                val token = bearerToken.substring(bearerPrefix.length)
                if (jwtTokenService.validateToken(token)) {
                    val authentication = jwtTokenService.getAuthentication(token)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
            filterChain.doFilter(request, response)
        } catch (ex: TokenInvalidException) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, ex)
        }
    }
}