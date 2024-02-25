package com.example.udo.config

import com.example.udo.service.authentication.JwtTokenService
import com.example.udo.service.authentication.UserDetailsServiceImpl
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userDetailsService: UserDetailsServiceImpl,
    private val jwtTokenService: JwtTokenService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .headers { headers -> headers.frameOptions { frameOptions -> frameOptions.disable() } }
            .sessionManagement { sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/register", "/login", "/books").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(JwtAuthenticationFilter(jwtTokenService, customAuthenticationFailureHandler()), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager? {
        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
            .and()
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun objectMapper(): ObjectMapper = jacksonObjectMapper()

    @Bean
    fun customAuthenticationFailureHandler(): CustomAuthenticationFailureHandler {
        return CustomAuthenticationFailureHandler(objectMapper())
    }
}