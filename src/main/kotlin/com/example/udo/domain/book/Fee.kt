package com.example.udo.domain.book

import jakarta.persistence.Embeddable

@Embeddable
class Fee(
    var value: Int
)
