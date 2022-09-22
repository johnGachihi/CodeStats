package me.johngachihi.codestats.mobile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform