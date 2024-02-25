package com.example.udo.service.circulation.impl

import com.example.udo.domain.book.BookEntity
import com.example.udo.domain.book.BookRepository
import com.example.udo.domain.user.Password
import com.example.udo.domain.user.UserEntity
import com.example.udo.domain.user.UserRepository
import com.example.udo.service.circulation.BookConsignService
import com.example.udo.service.circulation.dto.BookConsignRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

class BookConsignServiceImplTest {

    private val bookRepository: BookRepository = mock()
    private val userRepository: UserRepository = mock()
    private val bookConsignService = BookConsignServiceImpl(bookRepository, userRepository)

    @Test
    fun `consign saves a new book entity`() {
        // Given
        val userId = 1L
        val request = BookConsignRequest("Test Book", "123-456-789", 100)
        val userEntity = UserEntity("Test User", "test@example.com", "1234567890", Password("password"))
        val bookCaptor = argumentCaptor<BookEntity>()
        whenever(userRepository.getReferenceById(userId)).thenReturn(userEntity)

        // When
        bookConsignService.consign(userId, request)

        // Then
        verify(bookRepository).save(bookCaptor.capture())
        val savedBook = bookCaptor.firstValue
        assertEquals(request.title, savedBook.title)
        assertEquals(request.isbn, savedBook.isbn)
        assertEquals(request.rentFee, savedBook.fee.value)
        assertEquals(userEntity, savedBook.user)
    }
}