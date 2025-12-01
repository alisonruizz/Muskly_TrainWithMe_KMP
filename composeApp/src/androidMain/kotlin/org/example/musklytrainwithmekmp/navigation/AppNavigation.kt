package com.example.muskly_trainwithme_kmp.android.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.muskly_trainwithme_kmp.android.screens.GoalsScreen
import com.example.muskly_trainwithme_kmp.android.screens.TipsScreen
import com.example.muskly_trainwithme_kmp.android.screens.Train.TrainScreen
import com.example.muskly_trainwithme_kmp.android.screens.Home_Screen
import com.example.muskly_trainwithme_kmp.android.screens.ShopScreen

@Composable
fun AppNavigation() {
    var selectedTab by rememberSaveable { mutableStateOf(NavRoutes.Home) }

    Box(modifier = Modifier.fillMaxSize()) {

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
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )
    }
}
