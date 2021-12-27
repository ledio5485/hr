package io.led.hr.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
@EnableWebSecurity
@Order(1)
class APISecurityConfig(private val tokenAuthConfig: TokenAuthConfig) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.antMatcher("/api/**")
            .csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(apiKeyAuthFilter(tokenAuthConfig)).authorizeRequests().anyRequest().authenticated()
    }

    private fun apiKeyAuthFilter(tokenAuthConfig: TokenAuthConfig): APIKeyAuthFilter =
        APIKeyAuthFilter(tokenAuthConfig.tokenHeaderName).apply {
            setAuthenticationManager { it.apply { isAuthenticated = tokenAuthConfig.token == principal } }
        }
}

@Configuration
@ConfigurationProperties(prefix = "hr.http.auth")
class TokenAuthConfig {
    lateinit var tokenHeaderName: String
    lateinit var token: String
}

