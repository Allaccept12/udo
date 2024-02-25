package com.example.udo.service.circulation.validator

import com.example.udo.domain.book.BookEntity
import com.example.udo.domain.book.BookStatus
import com.example.udo.domain.user.UserEntity

class BookRentValidator {

    companion object {

        fun validateOwnerRentAttempt(user: UserEntity, books: List<BookEntity>) {
            books.forEach {
                if (it.user == user) {
                    throw IllegalArgumentException("자신이 위탁한 책은 대여할 수 없습니다.")
                }
            }
        }

        fun validateBooksAvailable(books: List<BookEntity>) {
            books.forEach {
                if (it.status != BookStatus.AVAILABLE) {
                    throw IllegalArgumentException("책 ${it.title}을 다른 유저가 이미 대여했습니다.")
                }
            }
        }

    }
}