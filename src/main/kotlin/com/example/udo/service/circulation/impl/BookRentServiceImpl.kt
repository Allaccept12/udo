package com.example.udo.service.circulation.impl

import com.example.udo.domain.book.BookEntity
import com.example.udo.domain.book.BookRepository
import com.example.udo.domain.book.BookStatus
import com.example.udo.domain.bookrent.BookRentEntity
import com.example.udo.domain.bookrent.BookRentRepository
import com.example.udo.domain.user.UserEntity
import com.example.udo.domain.user.UserRepository
import com.example.udo.event.BookRentEvent
import com.example.udo.exception.BookRentConcurrencyException
import com.example.udo.service.circulation.BookRentService
import com.example.udo.service.circulation.dto.BookRentRequest
import com.example.udo.service.circulation.validator.BookRentValidator
import org.springframework.context.ApplicationEventPublisher
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class BookRentServiceImpl(
    private val bookRentRepository: BookRentRepository,
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
): BookRentService {

    @Transactional
    override fun rent(userId: Long, request: BookRentRequest) {
        try {
            val user = userRepository.getReferenceById(userId)
            val books = bookRepository.findAllByIdIn(request.bookIds)
            BookRentValidator.validateOwnerRentAttempt(user, books)
            BookRentValidator.validateBooksAvailable(books)

            val bookRents = createBookRents(user, books)
            bookRentRepository.saveAll(bookRents)
            applicationEventPublisher.publishEvent(BookRentEvent(request.bookIds))
        } catch (e: ObjectOptimisticLockingFailureException) {
            throw BookRentConcurrencyException()
        }
    }

    private fun createBookRents(user: UserEntity, books: List<BookEntity>): List<BookRentEntity> {
        return books.map { book ->
            book.updateStatus(BookStatus.RENTED)
            BookRentEntity.create(book, user)
        }
    }
}