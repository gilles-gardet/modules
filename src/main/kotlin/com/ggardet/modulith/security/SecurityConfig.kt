package com.ggardet.modulith.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain = httpSecurity
        .authorizeHttpRequests { it.anyRequest().authenticated() }
        .csrf { it.disable() }
        .cors { it.disable() }
        .httpBasic { }
        .build()
}
