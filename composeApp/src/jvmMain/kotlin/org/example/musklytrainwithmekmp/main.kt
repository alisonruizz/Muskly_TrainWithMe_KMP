package org.example.musklytrainwithmekmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.musklytrainwithmekmp.navigation.AppNavigation
import org.example.musklytrainwithmekmp.screens.GoalsScreen
import org.example.musklytrainwithmekmp.screens.Home_Screen
import org.example.musklytrainwithmekmp.screens.ShopScreen
import org.example.musklytrainwithmekmp.screens.TipsScreen
import org.example.musklytrainwithmekmp.screens.TrainScreen
import org.example.musklytrainwithmekmp.theme.Muskly_TrainWithMeThemeDesktop

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Muskly_TrainWithMe_KMP",
    ) {
        Muskly_TrainWithMeThemeDesktop {
            AppNavigation()  // ⬅ Tu UI corriendo en escritorio
        }
    }
}
