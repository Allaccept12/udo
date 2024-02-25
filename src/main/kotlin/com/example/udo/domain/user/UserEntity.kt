package com.example.udo.domain.user

import com.example.udo.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    val name: String,
    val email: String,
    val phoneNumber: String,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "value", column = Column(name = "password"))
    )
    val password: Password
): BaseEntity() {

    fun verifyPassword(plainText: String) {
        if (!password.matches(plainText)) {
            throw IllegalArgumentException("Not Matched password")
        }
    }

    companion object {
        fun create(name: String, email: String, phoneNumber: String, password: String): UserEntity {
            return UserEntity(
                name,
                email,
                phoneNumber,
                Password(password)
            )
        }
    }
}