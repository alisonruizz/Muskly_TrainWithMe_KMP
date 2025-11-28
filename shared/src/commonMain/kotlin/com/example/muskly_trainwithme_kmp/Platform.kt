package com.example.muskly_trainwithme_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform