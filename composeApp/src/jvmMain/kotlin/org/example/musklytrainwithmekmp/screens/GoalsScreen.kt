package org.example.musklytrainwithmekmp.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import muskly_trainwithme_kmp.composeapp.generated.resources.Res
import muskly_trainwithme_kmp.composeapp.generated.resources.chiguicoin_png
import muskly_trainwithme_kmp.composeapp.generated.resources.goals_png
import org.example.musklytrainwithmekmp.Goal
import org.example.musklytrainwithmekmp.GoalsViewModel
import org.example.musklytrainwithmekmp.theme.Muskly_TrainWithMeThemeDesktop
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.DrawScope
//import androidx.compose.ui.graphics.drawscope.drawPath

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GoalsScreen(viewModel: GoalsViewModel? = null) {
    // IMPORTANT: keep single instance across recompositions
    val vm = viewModel ?: remember { GoalsViewModel() }

    val scope = rememberCoroutineScope()
    val goals by vm.goals.collectAsState()
    var sortOption by remember { mutableStateOf("All") }
    var rewardMessage by remember { mutableStateOf("") }
    var showReward by remember { mutableStateOf(false) }

    val sortedGoals = when (sortOption) {
        "Completed" -> goals.filter { it.completed }
        "Pending" -> goals.filter { !it.completed }
        else -> goals
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {

            Row(verticalAlignment = Alignment.Top) {
                Image(
                    org.jetbrains.compose.resources.painterResource(Res.drawable.goals_png),
                    contentDescription = "goals"
                )

                Spacer(Modifier.width(8.dp))

                SpeechBubble {
                    Text(
                        "Muskly dares you!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Your goals",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.weight(1f))

                SortMenu(
                    sortOption = sortOption,
                    onSelect = { sortOption = it }
                )
            }

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            ) {
                sortedGoals.forEach { goal ->
                    GoalItem(goal = goal) {
                        val reward = vm.completeGoal(goal)

                        if (reward > 0) {
                            rewardMessage = "🎉 Congratulations, you earned $reward chigui-coins!"
                            showReward = true
                            scope.launch {
                                kotlinx.coroutines.delay(2000)
                                showReward = false
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        AnimatedVisibility(
            visible = showReward,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(
                rewardMessage,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun GoalItem(goal: Goal, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (goal.completed)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.secondary,
                RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Text(
            goal.description,
            fontSize = 18.sp,
            color = if (goal.completed)
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.weight(1f)
        )

        if (!goal.completed) {
            Text(
                "${goal.reward}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
            Spacer(Modifier.width(4.dp))
            Image(
                painter = org.jetbrains.compose.resources.painterResource(Res.drawable.chiguicoin_png),
                contentDescription = "Moneda",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SpeechBubble(content: @Composable () -> Unit) {
    Box {
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp)) // <-- background blanco según pediste
                .padding(8.dp)
        ) {
            content()
        }

        Canvas(
            modifier = Modifier
                .size(30.dp)
                .offset(x = 24.dp, y = 60.dp)
        ) {
            val path = Path().apply {
                moveTo(size.width / 2f, size.height)
                lineTo(0f, 0f)
                lineTo(size.width, 0f)
                close()
            }

            drawPath(
                path = path,
                color = Color.White
            )
        }
    }
}

@Composable
fun SortMenu(sortOption: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text("Sort by: $sortOption")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("All") },
                onClick = { onSelect("All"); expanded = false }
            )
            DropdownMenuItem(
                text = { Text("Completed") },
                onClick = { onSelect("Completed"); expanded = false }
            )
            DropdownMenuItem(
                text = { Text("Pending") },
                onClick = { onSelect("Pending"); expanded = false }
            )
        }
    }
}

@Preview
@Composable
fun GoalsScreenPreview() {
    Muskly_TrainWithMeThemeDesktop {
        GoalsScreen()
    }
}
