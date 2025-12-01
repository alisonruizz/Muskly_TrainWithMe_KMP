package com.example.muskly_trainwithme_kmp.android.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muskly_trainwithme_kmp.Goal
import com.example.muskly_trainwithme_kmp.GoalsViewModel
import com.example.muskly_trainwithme_kmp.ui.theme.Muskly_TrainWithMeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import muskly_trainwithme_kmp.composeapp.generated.resources.Res
import muskly_trainwithme_kmp.composeapp.generated.resources.chiguicoin_png
import muskly_trainwithme_kmp.composeapp.generated.resources.goals_png
import org.jetbrains.compose.resources.painterResource



// Modelo de reto
data class Goal(
    val id: Int,
    val description: String,
    val reward: Int,
    var completed: Boolean = false
)

@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel = remember { GoalsViewModel() }, // KMP ViewModel
    onRewardEarned: (Int) -> Unit
) {
    var sortOption by rememberSaveable { mutableStateOf("All") }

    // Observamos StateFlow del ViewModel
    val goals by viewModel.goals.collectAsState()

    val sortedGoals = when (sortOption) {
        "Completed" -> goals.filter { it.completed }
        "Pending" -> goals.filter { !it.completed }
        else -> goals
    }

    var rewardMessage by remember { mutableStateOf("") }
    var showReward by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painterResource(Res.drawable.goals_png),
                        contentDescription = "goals"
                    )

                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 8.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                shape = speechBubbleShape()
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Musk dares you",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Your goals",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        TextButton(onClick = { expanded = true }) {
                            Text("Sort by: $sortOption")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("All") },
                                onClick = {
                                    sortOption = "All"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Completed") },
                                onClick = {
                                    sortOption = "Completed"
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Pending") },
                                onClick = {
                                    sortOption = "Pending"
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    sortedGoals.forEach { goal ->
                        GoalItem(
                            goal = goal,
                            onClick = {
                                val reward = viewModel.completeGoal(goal)
                                if (reward > 0) {
                                    onRewardEarned(reward)
                                    rewardMessage =
                                        "Congratulations, you earned $reward chigui-coins!"
                                    showReward = true
                                    coroutineScope.launch {
                                        delay(2000)
                                        showReward = false
                                    }
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            AnimatedVisibility(
                visible = showReward,
                enter = slideInVertically(
                    initialOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween(500)
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { fullHeight -> fullHeight },
                    animationSpec = tween(500)
                ) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 110.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(
                            color = Color(0xAA808080),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        rewardMessage,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun GoalItem(goal: Goal, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (goal.completed) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = goal.description,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f),
            color = if (goal.completed) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.secondaryContainer
        )

        if (!goal.completed) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${goal.reward}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
                Image(
                    painter = painterResource( Res.drawable.chiguicoin_png),
                    contentDescription = "Moneda",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// Función para el cuadro de diálogo
fun speechBubbleShape(): GenericShape {
    return GenericShape { size, _ ->
        val cornerRadius = 40f
        val pointerSize = 40f

        moveTo(cornerRadius, 0f)
        lineTo(size.width - cornerRadius, 0f)
        quadraticBezierTo(size.width, 0f, size.width, cornerRadius)
        lineTo(size.width, size.height - cornerRadius)
        quadraticBezierTo(size.width, size.height, size.width - cornerRadius, size.height)
        lineTo(pointerSize + cornerRadius, size.height)
        lineTo(pointerSize / 2, size.height + pointerSize)
        lineTo(cornerRadius, size.height)
        quadraticBezierTo(0f, size.height, 0f, size.height - cornerRadius)
        lineTo(0f, cornerRadius)
        quadraticBezierTo(0f, 0f, cornerRadius, 0f)
        close()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GoalsScreenPreview() {
    Muskly_TrainWithMeTheme {
        GoalsScreen(onRewardEarned = {})
    }
}
