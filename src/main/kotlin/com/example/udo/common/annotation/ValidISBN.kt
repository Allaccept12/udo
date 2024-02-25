package com.example.udo.common.annotation

import com.example.udo.common.validator.ISBNValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [ISBNValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidISBN(
    val message: String = "Invalid ISBN format",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)