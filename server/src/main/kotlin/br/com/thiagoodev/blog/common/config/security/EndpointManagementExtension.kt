package br.com.thiagoodev.blog.common.config.security

fun SecurityConfiguration.Companion.isPublicEndpoint(path: String): Boolean {
    return PUBLIC_ENDPOINTS.contains(path)
}