package com.example.udo.service.circulation.validator

import com.example.udo.domain.book.BookEntity
import com.example.udo.domain.book.BookStatus
import com.example.udo.domain.book.Fee
import com.example.udo.domain.user.Password
import com.example.udo.domain.user.UserEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BookRentValidatorTest {

    @Test
    fun `validateOwnerRentAttempt throws exception when user tries to rent their own book`() {
        val user = UserEntity("User", "user@example.com", "1234567890", Password("password"))
        val book = BookEntity("Book Title", "ISBN12345", Fee(100), user)

        assertThrows(IllegalArgumentException::class.java) {
            BookRentValidator.validateOwnerRentAttempt(user, listOf(book))
        }
    }

    @Test
    fun `validateOwnerRentAttempt does not throw exception when user rents others' book`() {
        val owner = UserEntity("Owner", "owner@example.com", "1234567890", Password("password"))
        val renter = UserEntity("Renter", "renter@example.com", "0987654321", Password("password"))
        val book = BookEntity("Book Title", "ISBN12345", Fee(100), owner)

        BookRentValidator.validateOwnerRentAttempt(renter, listOf(book))
    }

    @Test
    fun `validateBooksAvailable throws exception when book is already rented`() {
        val user = UserEntity("User", "user@example.com", "1234567890", Password("password"))
        val book = BookEntity("Book Title", "ISBN12345", Fee(100), user).apply { status = BookStatus.RENTED }

        assertThrows(IllegalArgumentException::class.java) {
            BookRentValidator.validateBooksAvailable(listOf(book))
        }
    }

    @Test
    fun `validateBooksAvailable does not throw exception when all books are available`() {
        val user = UserEntity("User", "user@example.com", "1234567890", Password("password"))
        val book = BookEntity("Book Title", "ISBN12345", Fee(100), user) // 기본 상태는 AVAILABLE

        BookRentValidator.validateBooksAvailable(listOf(book))
    }
}