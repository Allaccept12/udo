package com.example.udo.common.validator

import io.mockk.mockk
import jakarta.validation.ConstraintValidatorContext
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ISBNValidatorTest {

    private val isbnValidator = ISBNValidator()
    private val context = mockk<ConstraintValidatorContext>()

    @Test
    fun `ISBN with 13 digits is valid`() {
        assertTrue(isbnValidator.isValid("9780306406157", context))
    }

    @Test
    fun `ISBN with 3-10 digit format is valid`() {
        assertTrue(isbnValidator.isValid("0-321-14653-0", context))
    }

    @Test
    fun `ISBN with less than 13 digits is invalid`() {
        assertFalse(isbnValidator.isValid("123456789012", context))
    }

    @Test
    fun `ISBN with more than 13 digits is invalid`() {
        assertFalse(isbnValidator.isValid("12345678901234", context))
    }

    @Test
    fun `ISBN with invalid format is invalid`() {
        assertFalse(isbnValidator.isValid("123-45-678", context))
    }

    @Test
    fun `null ISBN is invalid`() {
        assertFalse(isbnValidator.isValid(null, context))
    }
}