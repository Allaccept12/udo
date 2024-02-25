package com.example.udo.common.validator

import com.example.udo.common.annotation.ValidISBN
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ISBNValidator : ConstraintValidator<ValidISBN, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return false
        }
        return value.matches("[0-9]{13}".toRegex()) || value.matches("[0-9]{3}-[0-9]{10}".toRegex())
    }
}