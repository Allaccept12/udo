package com.example.udo.controller.circulation

import com.example.udo.domain.book.SortType
import com.example.udo.domain.user.Password
import com.example.udo.service.authentication.model.CustomUserDetails
import com.example.udo.service.circulation.BookConsignService
import com.example.udo.service.circulation.BookInquiryService
import com.example.udo.service.circulation.BookRentService
import com.example.udo.service.circulation.dto.BookConsignRequest
import com.example.udo.service.circulation.dto.BookDetailResponse
import com.example.udo.service.circulation.dto.BookRentRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class BookCirculationControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var bookConsignmentService: BookConsignService

    @MockBean
    private lateinit var bookRentService: BookRentService

    @MockBean
    private lateinit var bookInquiryService: BookInquiryService

    @Test
    fun `consign book`() {
        val user = CustomUserDetails(1L, "username", "email", Password("password"))
        val request = BookConsignRequest("title", "1111111111111", 1000)
        val userId = 1L

        doNothing().`when`(bookConsignmentService).consign(userId, request)

        mockMvc.perform(post("/consign")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)

        verify(bookConsignmentService, times(1)).consign(userId, request)
    }

    @Test
    fun `consign book invalid isbn`() {
        val user = CustomUserDetails(1L, "username", "email", Password("password"))
        val request = BookConsignRequest("title", "111", 1000)
        val userId = 1L

        doNothing().`when`(bookConsignmentService).consign(userId, request)

        mockMvc.perform(post("/consign")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest)

        verify(bookConsignmentService, times(0)).consign(userId, request)
    }

    @Test
    fun `rent book`() {
        val user = CustomUserDetails(1L, "username", "email", Password("password"))
        val request = BookRentRequest(listOf(10L))
        val userId = 1L // 가정한 사용자 ID

        doNothing().`when`(bookRentService).rent(userId, request)

        mockMvc.perform(post("/rent")
            .with(user(user))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk)

        verify(bookRentService, times(1)).rent(userId, request)
    }

    @Test
    fun `get books`() {
        val books = listOf<BookDetailResponse>()
        val sortType = SortType.RENT_COUNT // 가정한 정렬 타입
        val pageable = PageRequest.of(0, 20) // 가정한 페이지 요청

        `when`(bookInquiryService.getConsignedBooks(sortType, pageable)).thenReturn(books)

        mockMvc.perform(get("/books")
            .param("sortType", sortType.toString())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().json(objectMapper.writeValueAsString(books)))

        verify(bookInquiryService, times(1)).getConsignedBooks(sortType, pageable)
    }
}