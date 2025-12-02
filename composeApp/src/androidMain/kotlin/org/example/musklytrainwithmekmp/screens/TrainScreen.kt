package com.example.muskly_trainwithme_kmp.android.screens.Train


import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.muskly_trainwithme_kmp.android.userID
import org.example.musklytrainwithmekmp.screens.AddExerciseSheet
import org.example.musklytrainwithmekmp.ui.theme.Muskly_TrainWithMeTheme
import muskly_trainwithme_kmp.composeapp.generated.resources.Res
import muskly_trainwithme_kmp.composeapp.generated.resources.musktrain
import org.example.musklytrainwithmekmp.TrainViewModel
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable

fun TrainScreen( viewModel: TrainViewModel = viewModel { TrainViewModel() }) {

    val firebasePetName by viewModel.petName.collectAsState()
    val routines by viewModel.routines.collectAsState()

    var petName by rememberSaveable { mutableStateOf("") }
    var selectedDay by rememberSaveable { mutableStateOf<String?>(null) }
    var showForm by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val isPetNameLocked by viewModel.isPetNameLocked.collectAsState()

    // Cargar datos apenas abra pantalla
    LaunchedEffect(Unit) {
        viewModel.loadRoutineFromFirebase(userId = userID)
    }

// Cuando el nombre desde Firebase cambie → actualizar TextField
    LaunchedEffect(firebasePetName) {
        if (firebasePetName.isNotEmpty()) {
            petName = firebasePetName   // ← aquí sí funciona
        }
    }


    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Create your new routine")},
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,

                    ),

                )
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .clickable(
                    // Si toca fuera del campo, quita el foco del teclado
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(12.dp))
                Image(
                    painter = painterResource(Res.drawable.musktrain),
                    contentDescription = null,
                    modifier = Modifier.height(140.dp).fillMaxWidth()
                )
            }


            // CARD CON TENIDO
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {

                // 🔹 TextField "Pet name"
                OutlinedTextField(
                    value = petName,
                    onValueChange = { petName = it },
                    enabled = !isPetNameLocked,   // 🔒 No editable si ya existe,
                    label = { Text("Pet name") },
                    shape = CircleShape,
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = Color.White,
                        // Color del contenedor cuando no está enfocado
                        unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
                        // Color del contenedor cuando está enfocado
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        //.background(MaterialTheme.colorScheme.outlineVariant)
                        .padding(vertical = 4.dp),)

                Spacer(Modifier.height(16.dp))

                // FlowRow = tus días semanales
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    days.forEach { day ->
                        Button(
                            onClick = { selectedDay = day },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedDay == day)
                                    MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier.padding(horizontal = 2.dp)
                        ) {
                            Text(day, fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

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
                                        .background(
                                            MaterialTheme.colorScheme.outlineVariant,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(ex.name, style = MaterialTheme.typography.titleSmall)
                                        Text("${ex.series}x${ex.reps} @ ${ex.weight}kg")
                                    }

                                    IconButton(onClick = {
                                        viewModel.removeExercise(day, index)
                                        viewModel.updateRoutineInFirebase(userID) { success ->  // ← CAMBIO AQUÍ
                                            if (success) println("Rutina actualizada con éxito")
                                            else println("Error al actualizar")
                                        }}) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Button(
                        onClick = { showForm = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Add exercise")
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        }

    }
    // Sheet para agregar ejercicio
    if (showForm) {
        AddExerciseSheet(
            onSave = { exercise ->
                viewModel.addExercise(selectedDay!!, exercise)
                viewModel.updateRoutineInFirebase(userID) { success ->  // ← CAMBIO AQUÍ
                    if (success) println("Rutina actualizada con éxito")
                    else println("Error al actualizar")
                }
                showForm = false
            },
            onDismiss = { showForm = false }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GoalsScreenPreview() {
    Muskly_TrainWithMeTheme {
        TrainScreen()
    }
}

