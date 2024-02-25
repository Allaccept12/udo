package com.example.udo.domain.book

import com.example.udo.domain.bookrent.BookRentEntity
import com.example.udo.domain.user.UserEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Root
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class BookRepositoryCustomImpl(@PersistenceContext private val entityManager: EntityManager): BookRepositoryCustom {

    override fun findPageByOrderBySortType(sort: SortType, pageable: Pageable): List<BookEntity> {
        val cb: CriteriaBuilder = entityManager.criteriaBuilder
        val cq: CriteriaQuery<BookEntity> = cb.createQuery(BookEntity::class.java)
        val book: Root<BookEntity> = cq.from(BookEntity::class.java)

        val order = when (sort) {
            SortType.RENT_COUNT -> cb.desc(cb.size(book.get<List<BookRentEntity>>("bookRentList")))
            SortType.LOW_FEE -> cb.asc(book.get<Fee>("fee").get<Int>("value"))
            SortType.RECENTLY_CONSIGNED -> cb.desc(book.get<LocalDateTime>("createdAt"))
        }

        cq.select(book)
            .where(cb.equal(book.get<BookStatus>("status"), BookStatus.AVAILABLE))
            .orderBy(order)
        book.fetch<BookEntity, UserEntity>("user", JoinType.LEFT)

        val typedQuery = entityManager.createQuery(cq)
        typedQuery.firstResult = pageable.offset.toInt()
        typedQuery.maxResults = pageable.pageSize

        return typedQuery.resultList
    }
}