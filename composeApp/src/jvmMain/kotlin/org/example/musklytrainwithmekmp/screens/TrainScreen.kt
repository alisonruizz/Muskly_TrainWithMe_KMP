package org.example.musklytrainwithmekmp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import muskly_trainwithme_kmp.composeapp.generated.resources.Res
import muskly_trainwithme_kmp.composeapp.generated.resources.musktrain
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TrainScreen(
    viewModel: TrainViewModel = viewModel { TrainViewModel() }
) {

    val routines by viewModel.routines.collectAsState()
    var petName by rememberSaveable { mutableStateOf("") }
    var selectedDay by rememberSaveable { mutableStateOf<String?>(null) }
    var showForm by rememberSaveable { mutableStateOf(false) }

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create your new routine") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {

            Spacer(Modifier.height(12.dp))
            Image(
                painter = painterResource(Res.drawable.musktrain),
                contentDescription = null,
                modifier = Modifier.height(160.dp).fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {

                //----------------- NAME FIELD -----------------//
                OutlinedTextField(
                    value = petName,
                    onValueChange = { petName = it },
                    label = { Text("Pet name") },
                    shape = CircleShape,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                //----------------- DAYS -----------------//
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    days.forEach { day ->
                        Button(
                            onClick = { selectedDay = day },
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    if (selectedDay == day) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Text(day, fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                //----------------- EXERCISES -----------------//
                selectedDay?.let { day ->
                    val exercises = routines[day] ?: emptyList()

                    if (exercises.isEmpty()) {
                        Text("No exercises added for $day", color = Color.Gray)
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            exercises.forEachIndexed { index, ex ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(ex.name, fontSize = 18.sp)
                                        Text("${ex.series}x${ex.reps} @ ${ex.weight}kg", fontSize = 14.sp)
                                    }

                                    IconButton(onClick = { viewModel.removeExercise(day, index) }) {
                                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        onClick = { showForm = true }
                    ) { Text("Add exercise") }
                }
            }
        }
    }

    //----------------- BOTTOM SHEET ADD -----------------//
    if (showForm) {
        AddExerciseSheet(
            onSave = { exercise ->
                viewModel.addExercise(selectedDay!!, exercise)
                showForm = false
            },
            onDismiss = { showForm = false }
        )
    }
}
