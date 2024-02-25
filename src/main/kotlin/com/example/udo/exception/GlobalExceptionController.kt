package com.example.udo.exception

import com.example.udo.common.log.logger
import com.example.udo.exception.dto.FailDto
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionController {

    @ExceptionHandler(
        ValidationException::class,
        IllegalArgumentException::class
    )
    fun handleException(exception: Exception): ResponseEntity<FailDto> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            FailDto(
                code = HttpStatus.BAD_REQUEST.value(),
                message = exception.message ?: "잘못된 요청입니다."
            ))
    }

    @ExceptionHandler(BookRentConcurrencyException::class)
    fun handleException(exception: BookRentConcurrencyException): ResponseEntity<FailDto> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            FailDto(
                code = HttpStatus.CONFLICT.value(),
                message = "대여 요청한 책을 다른 유저가 이미 대여했습니다."
            ))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class,)
    fun handleException(exception: MethodArgumentNotValidException): ResponseEntity<FailDto> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            FailDto(
                code = HttpStatus.BAD_REQUEST.value(),
                message = exception.bindingResult.fieldError?.defaultMessage ?: "잘못된 요청입니다."
            ))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class,)
    fun handleException(exception: HttpMessageNotReadableException): ResponseEntity<FailDto> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            FailDto(
                code = HttpStatus.BAD_REQUEST.value(),
                message = "필수 요청 파라미터가 누락되었습니다."
            ))
    }

    @ExceptionHandler(RentFeeNegativeNumberException::class)
    fun handleException(exception: RentFeeNegativeNumberException): ResponseEntity<FailDto> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            FailDto(
                code = HttpStatus.BAD_REQUEST.value(),
                message = "대여가격은 음수가 될 수 없습니다."
            ))
    }

    @ExceptionHandler(Exception::class)
    fun handleInternalException(exception: Exception): ResponseEntity<FailDto> {
        logger.error(exception.cause?.message)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            FailDto(
                code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message = "알 수 없는 오류가 발생했습니다."
            ))
    }
}