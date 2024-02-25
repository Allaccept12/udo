package com.example.udo.service.authentication

import com.example.udo.domain.user.UserRepository
import com.example.udo.service.authentication.model.CustomUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(userId: String): UserDetails {
        val userEntity = userRepository.getReferenceById(userId.toLong())

        return CustomUserDetails(
            id = userEntity.getId(),
            name = userEntity.name,
            email = userEntity.email,
            password = userEntity.password
        )
    }
}