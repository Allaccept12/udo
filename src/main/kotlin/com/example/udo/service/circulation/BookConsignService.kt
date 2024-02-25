package com.example.udo.service.circulation

import com.example.udo.service.circulation.dto.BookConsignRequest

interface BookConsignService {
    fun consign(userId: Long, request: BookConsignRequest)
}