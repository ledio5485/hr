package io.led.hr.security

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import javax.servlet.http.HttpServletRequest

class APIKeyAuthFilter(private val principalRequestHeader: String) : AbstractPreAuthenticatedProcessingFilter() {

    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest): String? {
        return request.getHeader(principalRequestHeader)
    }

    override fun getPreAuthenticatedCredentials(request: HttpServletRequest?): String {
        return "N/A"
    }
}