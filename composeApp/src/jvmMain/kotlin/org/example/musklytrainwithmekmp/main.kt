package org.example.musklytrainwithmekmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Muskly_TrainWithMe_KMP",
    ) {
        App()
    }
}