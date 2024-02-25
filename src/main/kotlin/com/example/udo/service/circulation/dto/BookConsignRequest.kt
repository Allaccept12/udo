package com.example.udo.service.circulation.dto

import com.example.udo.common.annotation.ValidISBN
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class BookConsignRequest(
    @field:NotBlank
    val title: String,

    @field:ValidISBN
    val isbn: String,

    @field:NotNull
    val rentFee: Int
)