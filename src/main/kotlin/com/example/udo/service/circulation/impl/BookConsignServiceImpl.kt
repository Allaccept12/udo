package com.example.udo.service.circulation.impl

import com.example.udo.domain.book.BookEntity
import com.example.udo.domain.book.BookRepository
import com.example.udo.domain.user.UserRepository
import com.example.udo.service.circulation.BookConsignService
import com.example.udo.service.circulation.dto.BookConsignRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookConsignServiceImpl(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository
): BookConsignService {

    @Transactional
    override fun consign(userId: Long, request: BookConsignRequest) {
        val userEntity = userRepository.getReferenceById(userId)
        BookEntity.create(
            title = request.title,
            isbn = request.isbn,
            fee = request.fee,
            user = userEntity
        ).let(bookRepository::save)
    }
}