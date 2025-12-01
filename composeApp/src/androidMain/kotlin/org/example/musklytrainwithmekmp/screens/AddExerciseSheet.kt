package com.example.muskly_trainwithme_kmp.android.Screens.Train


import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.example.muskly_trainwithme_kmp.Train.Exercise
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun AddExerciseSheet(onSave: (Exercise)->Unit, onDismiss:()->Unit) {

    var name by remember { mutableStateOf("") }
    var series by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && series.isNotBlank() && reps.isNotBlank() && weight.isNotBlank()) {
                        onSave(Exercise(name, series.toInt(), reps.toInt(), weight.toInt()))
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.outline
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Add Exercise") },
        text = {
            Column {
                CustomTextField(
                    label = "Exercise name",
                    value = name,
                    onValueChange = { name = it }
                )
                CustomTextField(
                    label = "Series",
                    value = series,
                    onValueChange = { if (it.all(Char::isDigit)) series = it },
                    keyboardType = KeyboardType.Number
                )
                CustomTextField(
                    label = "Reps",
                    value = reps,
                    onValueChange = { if (it.all(Char::isDigit)) reps = it },
                    keyboardType = KeyboardType.Number
                )
                CustomTextField(
                    label = "Weight (kg)",
                    value = weight,
                    onValueChange = { if (it.all(Char::isDigit)) weight = it },
                    keyboardType = KeyboardType.Number
                )
            }
        }
    )
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant,
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}
