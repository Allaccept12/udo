package com.example.udo.domain.bookrent

import com.example.udo.domain.BaseEntity
import com.example.udo.domain.book.BookEntity
import com.example.udo.domain.user.UserEntity
import jakarta.persistence.*

@Entity
@Table(name = "book_rents")
class BookRentEntity private constructor(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    val book: BookEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity
): BaseEntity() {

    companion object {
        fun create(book: BookEntity, user: UserEntity): BookRentEntity {
            return BookRentEntity(
                book,
                user
            )
        }
    }
}