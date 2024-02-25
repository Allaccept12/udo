package com.example.udo.service.circulation

import com.example.udo.domain.book.SortType
import com.example.udo.service.circulation.dto.BookDetailResponse
import org.springframework.data.domain.Pageable

interface BookInquiryService {
    fun getConsignedBooks(sortType: SortType, pageable: Pageable): List<BookDetailResponse>
}