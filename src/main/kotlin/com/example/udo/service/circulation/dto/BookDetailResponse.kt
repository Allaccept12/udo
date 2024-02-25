package com.example.udo.service.circulation.dto

data class BookDetailResponse(
    val id: Long,
    val title: String,
    val isbn: String,
    val fee: Int,
    val consignor: String
)