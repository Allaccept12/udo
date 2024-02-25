package com.example.udo.domain.bookrent

import org.springframework.data.jpa.repository.JpaRepository

interface BookRentRepository: JpaRepository<BookRentEntity, Long> {
}