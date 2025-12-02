package org.example.musklytrainwithmekmp.navigation

import androidx.compose.runtime.setValue


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.musklytrainwithmekmp.screens.GoalsScreen
import org.example.musklytrainwithmekmp.screens.TipsScreen
//import com.example.muskly_trainwithme_kmp.android.screens.Train.TrainScreen
import org.example.musklytrainwithmekmp.screens.Home_Screen
import org.example.musklytrainwithmekmp.screens.ShopScreen
import org.example.musklytrainwithmekmp.screens.TrainScreen

@Composable
fun AppNavigation(modifier: Modifier) {
    var selectedTab by rememberSaveable { mutableStateOf(NavRoutes.Home) }

    Box(modifier = modifier) {

        // -------- CONTENIDO PRINCIPAL --------
        when (selectedTab) {
            NavRoutes.Home -> Home_Screen()
            NavRoutes.Train -> TrainScreen()
            NavRoutes.Tips -> TipsScreen()
            NavRoutes.Goals -> GoalsScreen(onRewardEarned = {})
            NavRoutes.Shop -> ShopScreen()
        }

        // -------- BOTTOM BAR --------
        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}
