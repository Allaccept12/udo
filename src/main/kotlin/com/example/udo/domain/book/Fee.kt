package com.example.udo.domain.book

import com.example.udo.exception.RentFeeNegativeNumberException
import jakarta.persistence.Embeddable

@Embeddable
class Fee(val value: Int){
    init {
        if (this.value < 0) throw RentFeeNegativeNumberException()
    }
}
