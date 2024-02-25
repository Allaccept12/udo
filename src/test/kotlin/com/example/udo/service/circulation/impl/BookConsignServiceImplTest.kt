package com.example.udo.service.circulation.impl

import com.example.udo.domain.book.BookEntity
import com.example.udo.domain.book.BookRepository
import com.example.udo.domain.user.UserEntity
import com.example.udo.domain.user.UserRepository
import com.example.udo.service.circulation.dto.BookConsignRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever

class BookConsignServiceImplTest {

    private val bookRepository: BookRepository = mock()
    private val userRepository: UserRepository = mock()
    private val bookConsignService = BookConsignServiceImpl(bookRepository, userRepository)

    @Test
    fun `consign saves a new book entity`() {
        // Given
        val userId = 1L
        val request = BookConsignRequest("Test Book", "123-456-789", 100)
        val userEntity = UserEntity.create("Test User", "test@example.com", "1234567890", "password")
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