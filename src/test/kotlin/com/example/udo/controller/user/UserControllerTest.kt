package com.example.udo.controller.user

import com.example.udo.service.user.UserLoginService
import com.example.udo.service.user.UserRegisterService
import com.example.udo.service.user.dto.LoginRequest
import com.example.udo.service.user.dto.LoginResponse
import com.example.udo.service.user.dto.RegisterRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var userRegisterService: UserRegisterService

    @MockBean
    private lateinit var userLoginService: UserLoginService

    @Test
    fun `register user`() {
        val request = RegisterRequest("name", "email@naver.com", "phoneNumber", "password12")

        doNothing().`when`(userRegisterService).register(request)

        mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)

        verify(userRegisterService, times(1)).register(request)
    }

    @Test
    fun `register user invalid email format`() {
        val request = RegisterRequest("name", "email", "phoneNumber", "password12")

        mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest)

        verify(userRegisterService, times(0)).register(request)
    }

    @Test
    fun `register user invalid password only lowercase`() {
        val request = RegisterRequest("name", "email@naver.com", "phoneNumber", "password")

        mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest)

        verify(userRegisterService, times(0)).register(request)
    }

    @Test
    fun `register user invalid password only number`() {
        val request = RegisterRequest("name", "email@naver.com", "phoneNumber", "1234121")

        mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest)

        verify(userRegisterService, times(0)).register(request)
    }

    @Test
    fun `register user invalid password only upper case`() {
        val request = RegisterRequest("name", "email@naver.com", "phoneNumber", "PASSWORD")

        mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest)

        verify(userRegisterService, times(0)).register(request)
    }

    @Test
    fun `register user invalid password length`() {
        val request = RegisterRequest("name", "email@naver.com", "phoneNumber", "pass")

        mockMvc.perform(post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest)

        verify(userRegisterService, times(0)).register(request)
    }

    @Test
    fun `login user`() {
        val request = LoginRequest("email", "password")
        val expectedResponse = LoginResponse("token")

        `when`(userLoginService.login(request)).thenReturn(expectedResponse)

        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))

        verify(userLoginService, times(1)).login(request)
    }

}