package com.example.udo.domain.book

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface BookRepository: JpaRepository<BookEntity, Long>, BookRepositoryCustom {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT b FROM BookEntity b WHERE b.id IN :ids")
    fun findAllByIdIn(ids: List<Long>): List<BookEntity>
}