package com.example.udo.service.authentication

import com.example.udo.domain.user.Password
import com.example.udo.service.authentication.model.CustomUserDetails
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = [
    "security.jwt.token.secret-key=test-secret-key-dev-test-secret-key",
    "security.jwt.token.expire-length=3600000"
])
class JwtTokenServiceTest {

    @MockBean
    private lateinit var userDetailsService: UserDetailsServiceImpl

    @Autowired
    private lateinit var jwtTokenService: JwtTokenService

    @Test
    fun `createToken should return a valid JWT token`() {
        // Given
        val userId = 1L

        // when
        val token = jwtTokenService.createToken(userId)

        // Then
        assertNotNull(token)
        assertTrue(jwtTokenService.validateToken(token))
    }

    @Test
    fun `validateToken should validate the token successfully`() {
        // Given
        val userId = 1L
        val token = jwtTokenService.createToken(userId)
        val isValid = jwtTokenService.validateToken(token)

        // when - then
        assertTrue { isValid }
    }

    @Test
    fun `getAuthentication should return an authentication object for a valid token`() {
        // Given
        val userId = 1L
        val token = jwtTokenService.createToken(userId)
        val user = CustomUserDetails(
            id = 1L,
            name = "test",
            email = "test",
            password = Password("test")
        )
        Mockito.`when`(userDetailsService.loadUserByUsername(userId.toString())).thenReturn(user)

        // When
        val authentication = jwtTokenService.getAuthentication(token)

        // Then
        assertNotNull(authentication)
        val userDetails = authentication.principal as CustomUserDetails
        assertEquals(userId, userDetails.getId())
    }
}