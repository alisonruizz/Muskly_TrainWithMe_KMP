package com.example.muskly_trainwithme_kmp.android.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.muskly_trainwithme.goalsscreen.GoalsScreen
import com.example.muskly_trainwithme_kmp.android.Screens.TipsScreen
import com.example.muskly_trainwithme_kmp.android.Screens.Train.TrainScreen
import com.example.muskly_trainwithme_kmp.android.screens.*

@Composable
fun AppNavigation() {
    var selectedTab by rememberSaveable { mutableStateOf(NavRoutes.Home) }

    Box(modifier = Modifier.fillMaxSize()) {

        // -------- CONTENIDO PRINCIPAL --------
        when (selectedTab) {
            NavRoutes.Home -> HomeScreen()
            NavRoutes.Train -> TrainScreen()
            NavRoutes.Tips -> TipsScreen()
            NavRoutes.Goals -> GoalsScreen(onRewardEarned = {})
            NavRoutes.Shop -> Shop_Screen()
        }

        // -------- BOTTOM BAR --------
        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}
