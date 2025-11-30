package com.example.muskly_trainwithme_kmp.android.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigationBar(
    selectedTab: NavRoutes,
    onTabSelected: (NavRoutes) -> Unit,
    modifier: Modifier = Modifier
) {
    // Íconos compatibles para Android e iOS
    val items = listOf(
        Triple(NavRoutes.Home, "Home", Icons.Default.Home),
        Triple(NavRoutes.Train, "Train", Icons.Default.Person),
        Triple(NavRoutes.Tips, "Tips", Icons.Default.List),
        Triple(NavRoutes.Goals, "Goals", Icons.Default.Star),
        //Triple(NavRoutes.Shop, "Shop", Icons.Default.ShoppingCart)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = MaterialTheme.shapes.large,
                    clip = false
                )
                .clip(MaterialTheme.shapes.large)
                .background(Color.White) // Fondo único
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEach { (route, label, icon) ->

                val isSelected = selectedTab == route
                val selectedColor = Color(0xFF00C896)
                val unselectedColor = Color.Gray

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onTabSelected(route) }
                        .padding(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) selectedColor else unselectedColor,
                        modifier = Modifier.size(22.dp)
                    )

                    Text(
                        text = label,
                        fontSize = 11.sp,
                        color = if (isSelected) selectedColor else unselectedColor
                    )
                }
            }
        }
    }
}
