package com.example.udo.domain.book

import com.example.udo.domain.bookrent.BookRentEntity
import com.example.udo.domain.bookrent.BookRentRepository
import com.example.udo.domain.user.Password
import com.example.udo.domain.user.UserEntity
import com.example.udo.domain.user.UserRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@DataJpaTest
class BookRepositoryCustomImplTest {

    @Autowired
    private lateinit var bookRepositoryCustom: BookRepositoryCustomImpl

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var bookRentRepository: BookRentRepository

    @PersistenceContext
    private lateinit var em: EntityManager

    @Test
    fun `test findPageByOrderBySortType sortType RENT_COUNT`() {
        val sortType = SortType.RENT_COUNT
        val userEntity = createUser()
        val userEntity2 = createUser()

        val bookEntity1 = createBook(1000, userEntity)
        val bookEntity2 = createBook(1000, userEntity)
        val bookEntity3 = createBook(1000, userEntity)

        rentBook(bookEntity1, userEntity2, 2)
        rentBook(bookEntity2, userEntity2, 1)
        rentBook(bookEntity3, userEntity2, 3)

        val pageable = PageRequest.of(0, 20)

        val books = bookRepositoryCustom.findPageByOrderBySortType(sortType, pageable)

        assertEquals(3, books.size)
        assertEquals(bookEntity3.id, books[0].id)
        assertEquals(bookEntity1.id, books[1].id)
        assertEquals(bookEntity2.id, books[2].id)
    }

    @Test
    fun `test findPageByOrderBySortType sortType LOW_FEE`() {
        val sortType = SortType.LOW_FEE
        val userEntity = createUser()

        val bookEntity1 = createBook(1100, userEntity)
        val bookEntity2 = createBook(1000, userEntity)
        val bookEntity3 = createBook(1300, userEntity)

        val pageable = PageRequest.of(0, 20)

        val books = bookRepositoryCustom.findPageByOrderBySortType(sortType, pageable)

        assertEquals(3, books.size)
        assertEquals(bookEntity2.id, books[0].id)
        assertEquals(bookEntity1.id, books[1].id)
        assertEquals(bookEntity3.id, books[2].id)
    }

    @Test
    fun `test findPageByOrderBySortType sortType RECENTLY_CONSIGNED`() {
        val sortType = SortType.RECENTLY_CONSIGNED
        val userEntity = createUser()

        val bookEntity2 = createBook(1000, userEntity)
        val bookEntity3 = createBook(1000, userEntity)
        val bookEntity1 = createBook(1000, userEntity)

        val pageable = PageRequest.of(0, 20)

        val books = bookRepositoryCustom.findPageByOrderBySortType(sortType, pageable)

        assertEquals(3, books.size)
        assertEquals(bookEntity1.id, books[0].id)
        assertEquals(bookEntity3.id, books[1].id)
        assertEquals(bookEntity2.id, books[2].id)
    }

    fun createUser(): UserEntity {
        val user = UserEntity("name", "email", "phoneNumber", Password("password"))
        userRepository.save(user)
        em.persist(user)
        return user
    }

    fun createBook(fee: Int, user: UserEntity): BookEntity {
        val book = BookEntity("title1", "isbn", Fee(fee), user)
        bookRepository.save(book)
        em.persist(book)
        return book
    }

    fun rentBook(book: BookEntity, user: UserEntity, times: Int) {
        repeat(times) {
            val bookRent = BookRentEntity(book, user)
            bookRentRepository.save(bookRent)
            em.persist(bookRent)
        }
    }

}