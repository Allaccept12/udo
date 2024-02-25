package com.example.udo.service.user.dto

import jakarta.validation.constraints.*

data class RegisterRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:Email(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
        message = "Invalid email format"
    )
    val email: String,

    @field:NotBlank(message = "Phone number is required")
    val phoneNumber: String,

    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9]).+\$|(?=.*[a-z])(?=.*[A-Z]).+\$",
        message = "Invalid password format"
    )
    val password: String,
)