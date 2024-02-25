package com.example.udo.domain.book

import com.example.udo.domain.BaseEntity
import com.example.udo.domain.bookrent.BookRentEntity
import com.example.udo.domain.user.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "books")
class BookEntity(
    val title: String,
    val isbn: String,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "value", column = Column(name = "fee"))
    )
    val fee: Fee,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    val user: UserEntity
): BaseEntity() {

    @Enumerated(EnumType.STRING)
    var status: BookStatus = BookStatus.AVAILABLE

    @OneToMany(mappedBy = "book")
    val bookRentList: List<BookRentEntity> = emptyList()

    @CreationTimestamp
    val createdAt: LocalDateTime? = null

    @Version
    val version: Long = 0L

    fun updateStatus(status: BookStatus): BookEntity {
        this.status = status
        return this
    }

}