package dev.thiagosouto.domain

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform