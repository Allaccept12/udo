package com.example.udo.service.authentication

import com.example.udo.exception.TokenInvalidException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtTokenService(
    private val userDetailsService: UserDetailsServiceImpl
) {

    @Value("\${security.jwt.token.secret-key:secret}")
    private lateinit var secretPlainKey: String

    @Value("\${security.jwt.token.expire-length}")
    private var validityInMilliseconds: Long? = null

    private var secretKey: SecretKey? = null

    fun createToken(userId: Long): String {
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds!!)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(getSecretKey())
            .compact()
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
            return true
        } catch (e: Exception) {
            throw TokenInvalidException()
        }
    }

    fun getAuthentication(token: String): Authentication {
        val userDetails: UserDetails = userDetailsService.loadUserByUsername(getSubject(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun getSubject(token: String): String {
        val claims = Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(token)
            .body

        return claims.subject
    }

    private fun getSecretKey(): SecretKey {
        secretKey?.let { return it }
        return createSecretKey(secretPlainKey).also { secretKey = it }
    }

    private fun createSecretKey(secret: String): SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())
}