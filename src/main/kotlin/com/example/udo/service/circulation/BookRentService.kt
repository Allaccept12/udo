package com.example.udo.service.circulation

import com.example.udo.service.circulation.dto.BookRentRequest

interface BookRentService {
    fun rent(userId: Long, request: BookRentRequest)
}