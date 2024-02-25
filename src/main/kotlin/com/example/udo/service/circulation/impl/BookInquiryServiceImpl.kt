package com.example.udo.service.circulation.impl

import com.example.udo.domain.book.BookRepository
import com.example.udo.domain.book.SortType
import com.example.udo.service.circulation.BookInquiryService
import com.example.udo.service.circulation.dto.BookDetailResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookInquiryServiceImpl(private val bookRepository: BookRepository): BookInquiryService {

    override fun getConsignedBooks(sortType: SortType, pageable: Pageable): List<BookDetailResponse> {
        return bookRepository.findPageByOrderBySortType(sortType, pageable).map {
            BookDetailResponse(
                id = it.getId(),
                title = it.title,
                isbn = it.isbn,
                fee = it.fee.value,
                it.user.name
            )
        }
    }
}