package com.example.spring.sec.controller

import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * обрабатывает 403 ошибку перенаправляя в случае ее вызова на /403 страницу
 */
@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler::class.java)
    }

    override fun handle(httpServletRequest: HttpServletRequest,
                        httpServletResponse: HttpServletResponse,
                        e: AccessDeniedException) {
        val auth = SecurityContextHolder.getContext().authentication

        if (auth != null) {
            LOGGER.info("User '${auth.name}' attempted to access the protected URL: ${httpServletRequest.requestURI}")
        }

        httpServletResponse.sendRedirect(httpServletRequest.contextPath + "/403")
    }
}