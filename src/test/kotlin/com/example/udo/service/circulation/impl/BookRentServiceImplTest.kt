package com.example.udo.service.circulation.impl

import com.example.udo.domain.book.BookEntity
import com.example.udo.domain.book.BookRepository
import com.example.udo.domain.book.Fee
import com.example.udo.domain.bookrent.BookRentEntity
import com.example.udo.domain.bookrent.BookRentRepository
import com.example.udo.domain.user.Password
import com.example.udo.domain.user.UserEntity
import com.example.udo.domain.user.UserRepository
import com.example.udo.event.BookRentEvent
import com.example.udo.exception.BookRentConcurrencyException
import com.example.udo.service.circulation.dto.BookRentRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever
import org.springframework.context.ApplicationEventPublisher
import org.springframework.orm.ObjectOptimisticLockingFailureException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class BookRentServiceImplTest {

    private val bookRentRepository: BookRentRepository = mock()
    private val bookRepository: BookRepository = mock()
    private val userRepository: UserRepository = mock()
    private val applicationEventPublisher: ApplicationEventPublisher = mock()

    private val bookRentService = BookRentServiceImpl(
        bookRentRepository,
        bookRepository,
        userRepository,
        applicationEventPublisher
    )

    @Test
    fun `rent books successfully`() {
        // Given
        val userId = 1L
        val bookIds = listOf(1L, 2L)
        val request = BookRentRequest(bookIds)
        val user = UserEntity("name", "email", "phoneNumber", Password("password"))
        val user2 = UserEntity("name", "email", "phoneNumber", Password("password"))
        val books = listOf(
            BookEntity("title1", "isbn1", Fee(100), user),
            BookEntity("title2", "isbn2", Fee(200), user)
        )

        whenever(userRepository.getReferenceById(userId)).thenReturn(user2)
        whenever(bookRepository.findAllByIdIn(bookIds)).thenReturn(books)

        // When
        bookRentService.rent(userId, request)

        // Then
        val bookRentCaptor = argumentCaptor<List<BookRentEntity>>()
        verify(bookRentRepository).saveAll(bookRentCaptor.capture())

        val capturedBookRents = bookRentCaptor.firstValue
        assertEquals(2, capturedBookRents.size)
        assertEquals(books[0], capturedBookRents[0].book)
        assertEquals(user2, capturedBookRents[0].user)
        assertEquals(books[1], capturedBookRents[1].book)
        assertEquals(user2, capturedBookRents[1].user)

        verify(applicationEventPublisher).publishEvent(any<BookRentEvent>())
    }

    @Test
    fun `rent concurrency issues correctly`() {
        val userId = 1L
        val request = BookRentRequest(listOf(1L, 2L))

        whenever(userRepository.getReferenceById(userId)).thenReturn(mock())
        whenever(bookRepository.findAllByIdIn(request.bookIds)).thenThrow(ObjectOptimisticLockingFailureException::class.java)

        val future1 = CompletableFuture.runAsync { bookRentService.rent(userId, request) }
        val future2 = CompletableFuture.runAsync { bookRentService.rent(userId, request) }

        val assertThrows = assertThrows(ExecutionException::class.java) {
            CompletableFuture.allOf(future1, future2).get()
        }

        assertTrue(assertThrows.cause is BookRentConcurrencyException)
    }
}