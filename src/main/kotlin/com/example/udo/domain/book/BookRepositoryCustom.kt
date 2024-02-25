package com.example.udo.domain.book

import org.springframework.data.domain.Pageable

interface BookRepositoryCustom {

    fun findPageByOrderBySortType(sort: SortType, pageable: Pageable): List<BookEntity>
}