package org.example.musklytrainwithmekmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.musklytrainwithmekmp.navigation.AppNavigation
import org.example.musklytrainwithmekmp.theme.Muskly_TrainWithMeThemeDesktop

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Muskly_TrainWithMe_KMP",
    ) {
        Muskly_TrainWithMeThemeDesktop {

            var showCredits by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {

                // ---------- UI principal ----------
                AppNavigation(modifier = Modifier.fillMaxSize())

                // ---------- Botón de créditos (arriba derecha) ----------
                IconButton(
                    onClick = { showCredits = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Help,
                        contentDescription = "Help / Credits",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                // ---------- Ventana emergente de créditos ----------
                if (showCredits) {
                    AlertDialog(
                        onDismissRequest = { showCredits = false },
                        confirmButton = {
                            Button(
                                onClick = { showCredits = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            ) {
                                Text("Close", color = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                        },
                        title = {
                            Text("About the app", fontSize = 20.sp)
                        },
                        text = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Muskly is an app to keep you motivated at the gym with the help of a virtual pet. Track your routines and complete challenges to boost your progress. " +
                                            "The points you earn help your pet become stronger and unlock accessories and skins.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Justify
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Credits:\nAlison Daniela Ruiz\nJuan José Ángel Durán",
                                    style = MaterialTheme.typography.bodyLarge,
                                    lineHeight = 24.sp,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center
                                )
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
        }
    }
}
