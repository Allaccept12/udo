package com.example.udo.listener

import com.example.udo.domain.book.BookRepository
import com.example.udo.domain.book.BookStatus
import com.example.udo.event.BookRentEvent
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.time.Instant

@Component
class BookRentListener(
    private val bookRepository: BookRepository,
    private val taskScheduler: TaskScheduler
) {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun listen(event: BookRentEvent) {
        val runAt = Instant.now().plusSeconds(10)
        taskScheduler.schedule({
            val updatedBooks = bookRepository.findAllById(event.rentedBookIds)
                .map { it.updateStatus(BookStatus.AVAILABLE) }
            bookRepository.saveAll(updatedBooks)
        }, runAt)
    }

}