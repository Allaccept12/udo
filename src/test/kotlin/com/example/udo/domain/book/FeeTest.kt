package com.example.udo.domain.book

import com.example.udo.exception.RentFeeNegativeNumberException
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class FeeTest {

    @Test
    fun `create Fee success`() {
        assertDoesNotThrow { Fee(100) }
    }

    @Test
    fun `create Fee throws exception when value negative number`() {
        assertThrows(RentFeeNegativeNumberException::class.java) {
            Fee(-100)
        }
    }
}