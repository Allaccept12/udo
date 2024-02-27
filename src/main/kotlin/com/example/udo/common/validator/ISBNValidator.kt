package com.example.udo.common.validator

import com.example.udo.common.annotation.ValidISBN
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ISBNValidator : ConstraintValidator<ValidISBN, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return false
        }
        val cleanValue = value.replace("-", "")
        return when (cleanValue.length) {
            13 -> isValidISBN13(cleanValue)
            10 -> isValidISBN10(cleanValue)
            else -> false
        }
    }

    private fun isValidISBN13(isbn: String): Boolean {
        val sum = isbn.substring(0, 12).mapIndexed { index, c ->
            c.toString().toInt() * if (index % 2 == 0) 1 else 3
        }.sum()

        val checksum = (10 - (sum % 10)) % 10
        return checksum == isbn.last().toString().toInt()
    }

    private fun isValidISBN10(isbn: String): Boolean {
        val sum = isbn.substring(0, 9).mapIndexed { index, c ->
            c.toString().toInt() * (10 - index)
        }.sum()

        val checksum = sum % 11
        val lastChar = isbn.last()

        return if (checksum == 10) lastChar == 'X' else checksum == lastChar.toString().toInt()
    }
}