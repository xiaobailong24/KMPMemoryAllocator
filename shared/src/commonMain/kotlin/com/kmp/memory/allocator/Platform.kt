package com.kmp.memory.allocator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform