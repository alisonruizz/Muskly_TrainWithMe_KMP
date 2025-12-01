package com.example.muskly_trainwithme_kmp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.example.musklytrainwithmekmp.screens.GoalsScreen
import org.example.musklytrainwithmekmp.navigation.AppNavigation
import org.example.musklytrainwithmekmp.ui.theme.Muskly_TrainWithMeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Muskly_TrainWithMeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}


@Preview
@Composable
fun DefaultPreview() {
    Muskly_TrainWithMeTheme {
        GoalsScreen(onRewardEarned = {})
    }
}