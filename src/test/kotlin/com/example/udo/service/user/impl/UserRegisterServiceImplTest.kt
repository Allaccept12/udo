package com.example.udo.service.user.impl

import com.example.udo.domain.user.UserEntity
import com.example.udo.domain.user.UserRepository
import com.example.udo.service.user.dto.RegisterRequest
import com.example.udo.service.user.validator.UserRegisterValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class UserRegisterServiceImplTest {

    private val userRepository: UserRepository = mock()
    private val userRegisterValidator: UserRegisterValidator = mock()
    private val userRegisterService: UserRegisterServiceImpl = UserRegisterServiceImpl(userRepository, userRegisterValidator)

    @Test
    fun `register successfully with new email`() {
        // Given
        val request = RegisterRequest("name", "email@example.com", "phoneNumber", "password")
        doNothing().`when`(userRegisterValidator).validateExistingEmail(request.email)

        // When
        userRegisterService.register(request)

        // Then
        argumentCaptor<UserEntity>().apply {
            verify(userRepository).save(capture())
            assertEquals(request.email, firstValue.email)
            assertEquals(request.name, firstValue.name)
        }
    }

    @Test
    fun `register fails when email already exists`() {
        // Given
        val request = RegisterRequest("name", "existing@example.com", "phoneNumber", "password")
        doThrow(IllegalArgumentException("Email already exists")).`when`(userRegisterValidator).validateExistingEmail(request.email)

        // When & Then
        assertThrows(IllegalArgumentException::class.java) {
            userRegisterService.register(request)
        }
    }
}