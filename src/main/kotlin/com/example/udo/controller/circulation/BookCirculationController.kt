package com.example.udo.controller.circulation

import com.example.udo.domain.book.SortType
import com.example.udo.service.authentication.model.CustomUserDetails
import com.example.udo.service.circulation.BookConsignService
import com.example.udo.service.circulation.BookInquiryService
import com.example.udo.service.circulation.BookRentService
import com.example.udo.service.circulation.dto.BookConsignRequest
import com.example.udo.service.circulation.dto.BookDetailResponse
import com.example.udo.service.circulation.dto.BookRentRequest
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
class BookCirculationController(
    private val bookConsignmentService: BookConsignService,
    private val bookRentService: BookRentService,
    private val bookInquiryService: BookInquiryService
) {

    @PostMapping("/book/consign")
    fun consign(
        @AuthenticationPrincipal user: CustomUserDetails,
        @Valid @RequestBody request: BookConsignRequest
    ) {
        bookConsignmentService.consign(user.getId(), request)
    }

    @PostMapping("/book/rent")
    fun rent(
        @AuthenticationPrincipal user: CustomUserDetails,
        @RequestBody bookRentRequest: BookRentRequest
    ) {
        bookRentService.rent(user.getId(), bookRentRequest)
    }

    @GetMapping("/books")
    fun getBooks(
        @RequestParam sortType: SortType,
        @PageableDefault(size = 20) pageable: Pageable
    ): List<BookDetailResponse> {
        return bookInquiryService.getConsignedBooks(sortType, pageable)
    }
}