package com.example.muskly_trainwithme_kmp.android

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.muskly_trainwithme_kmp.GoalsViewModel
import com.example.muskly_trainwithme_kmp.R

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

    Scaffold (
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
                        painter = painterResource(id = R.drawable.goals_png),
                        contentDescription = "Mascota",
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Fit
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
                            text = stringResource(R.string.musk_message),
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
                                        "🎉 Congratulations, you earned $reward chigui-coins!"
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
                    .padding(bottom = 16.dp)
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
