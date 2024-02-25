package com.example.udo.service.user.impl

import com.example.udo.domain.user.UserEntity
import com.example.udo.domain.user.UserRepository
import com.example.udo.service.authentication.JwtTokenService
import com.example.udo.service.user.UserLoginService
import com.example.udo.service.user.dto.LoginRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class UserLoginServiceImplTest {

    private val userRepository: UserRepository = mock()
    private val jwtTokenService: JwtTokenService = mock()
    private val userLoginService: UserLoginService = UserLoginServiceImpl(userRepository, jwtTokenService)

    @Test
    fun `login succeeds with correct credentials`() {
        // Given
        val email = "user@example.com"
        val userEntity = UserEntity.create("name", email, "1234567890", "password")
        userEntity.id = 10L
        val loginRequest = LoginRequest(email, "password")
        val expectedToken = "token"

        whenever(userRepository.findByEmail(email)).thenReturn(userEntity)
        whenever(jwtTokenService.createToken(any())).thenReturn(expectedToken)

        // When
        val response = userLoginService.login(loginRequest)

        // Then
        assertEquals(expectedToken, response.token)
    }

    @Test
    fun `login fails when user does not exist`() {
        // Given
        val loginRequest = LoginRequest("nonexistent@example.com", "password")
        whenever(userRepository.findByEmail(any())).thenReturn(null)

        // When & Then
        assertThrows(IllegalArgumentException::class.java) {
            userLoginService.login(loginRequest)
        }
    }

    @Test
    fun `login fails with incorrect password`() {
        // Given
        val email = "user@example.com"
        val userEntity = UserEntity.create("name", email, "1234567890", "password")
        userEntity.id = 10L
        val loginRequest = LoginRequest(email, "incorrectPassword")

        whenever(userRepository.findByEmail(email)).thenReturn(userEntity)

        // When & Then
        assertThrows(IllegalArgumentException::class.java) {
            userLoginService.login(loginRequest)
        }
    }
}