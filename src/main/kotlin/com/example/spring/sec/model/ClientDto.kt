package com.example.spring.sec.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.provider.ClientDetails

data class ClientDto(
        val id: String,
        var secret: String
) : ClientDetails {
    override fun isSecretRequired() = true

    override fun getAdditionalInformation() = mapOf<String, Any>()

    override fun getAccessTokenValiditySeconds() = null

    override fun getResourceIds() = setOf<String>()

    override fun getClientId() = id

    override fun isAutoApprove(scope: String?) = false

    override fun getAuthorities() = listOf<GrantedAuthority>()

    override fun getRefreshTokenValiditySeconds() = null

    override fun getClientSecret() = secret

    override fun getRegisteredRedirectUri() = setOf<String>()

    override fun isScoped() = true

    override fun getScope() = setOf("read")

    override fun getAuthorizedGrantTypes() = setOf("password", "authorization_code", "refresh_token")
}