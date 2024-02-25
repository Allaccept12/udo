package com.example.udo.service.authentication.model

import com.example.udo.domain.user.Password
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class CustomUserDetails(
    private val id: Long,
    private val name: String,
    private val email: String,
    private val password: Password
) : UserDetails {

    fun getId() = id

    override fun getAuthorities(): Collection<GrantedAuthority>? = null

    override fun getPassword(): String = this.password.value

    override fun getUsername(): String = this.email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}