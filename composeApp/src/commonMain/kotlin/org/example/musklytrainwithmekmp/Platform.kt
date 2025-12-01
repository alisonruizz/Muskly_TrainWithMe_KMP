package org.example.musklytrainwithmekmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform