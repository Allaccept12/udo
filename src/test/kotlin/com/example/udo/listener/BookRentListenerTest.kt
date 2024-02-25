package com.example.udo.listener

import com.example.udo.domain.book.BookEntity
import com.example.udo.domain.book.BookRepository
import com.example.udo.domain.book.BookStatus
import com.example.udo.domain.book.Fee
import com.example.udo.domain.user.Password
import com.example.udo.domain.user.UserEntity
import com.example.udo.event.BookRentEvent
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.scheduling.TaskScheduler
import java.time.Instant

class BookRentListenerTest {

    private val bookRepository: BookRepository = mock()
    private val taskScheduler: TaskScheduler = mock()
    private val bookRentListener: BookRentListener = BookRentListener(bookRepository, taskScheduler)

    @Test
    fun `verify scheduled task updates book status to AVAILABLE`() {
        // Given
        val rentedBookIds = listOf(1L, 2L)
        val bookRentEvent = BookRentEvent(rentedBookIds)
        val books = rentedBookIds.map {
            BookEntity("Title $it", "ISBN$it", Fee(100), UserEntity("name", "email", "1234567890", Password("password"))).apply {
                id = it
                status = BookStatus.RENTED
            }
        }
        whenever(bookRepository.findAllById(rentedBookIds)).thenReturn(books)

        doAnswer { invocation ->
            val runnable = invocation.getArgument(0, Runnable::class.java)
            runnable.run()
            null
        }.whenever(taskScheduler).schedule(any(), any<Instant>())

        // When
        bookRentListener.listen(bookRentEvent)

        // Then
        val savedBooksCaptor = argumentCaptor<List<BookEntity>>()
        verify(bookRepository).saveAll(savedBooksCaptor.capture())
        assertTrue(savedBooksCaptor.allValues.flatten().all { it.status == BookStatus.AVAILABLE })
    }
}
