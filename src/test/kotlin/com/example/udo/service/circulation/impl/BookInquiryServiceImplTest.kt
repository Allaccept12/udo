package com.example.udo.service.circulation.impl

import com.example.udo.domain.book.BookEntity
import com.example.udo.domain.book.BookRepository
import com.example.udo.domain.book.Fee
import com.example.udo.domain.book.SortType
import com.example.udo.domain.user.Password
import com.example.udo.domain.user.UserEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

class BookInquiryServiceImplTest {

    private val bookRepository: BookRepository = mock()
    private val bookInquiryService = BookInquiryServiceImpl(bookRepository)

    @Test
    fun `getConsignedBooks returns list of BookDetailResponse`() {
        // Given
        val sortType = SortType.RECENTLY_CONSIGNED
        val pageable: Pageable = PageRequest.of(0, 10)
        val bookEntity1 = BookEntity(
            "Test Book 1",
            "123-456-789",
            Fee(100),
            UserEntity("User 1", "user1@example.com", "1234567890", Password("password"))
        )
        bookEntity1.id = 10L
        val bookEntity2 = BookEntity(
            "Test Book 2",
            "987-654-321",
            Fee(200),
            UserEntity("User 2", "user2@example.com", "0987654321", Password("password"))
        )
        bookEntity2.id = 20L
        val books = listOf(bookEntity1, bookEntity2)

        whenever(bookRepository.findPageByOrderBySortType(sortType, pageable)).thenReturn(books)

        // When
        val result = bookInquiryService.getConsignedBooks(sortType, pageable)

        // Then
        assertEquals(2, result.size)
        assertEquals("Test Book 1", result[0].title)
        assertEquals("Test Book 2", result[1].title)
        verify(bookRepository, times(1)).findPageByOrderBySortType(sortType, pageable)
    }
}