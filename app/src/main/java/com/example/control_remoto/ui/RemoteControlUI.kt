package com.example.control_remoto.ui

import com.example.control_remoto.MainActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun RemoteControlUI(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val bleServer = (context as? MainActivity)?.bleServer
    val buttonElevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)

    fun showMessage(message: String) {
        android.widget.Toast.makeText(
            context,
            "Acción: $message",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }

    Row(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Columna izquierda (Bluetooth, GiroIzq/GiroDer)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxHeight()
        ) {
            IconButton(
                onClick = {
                    showMessage("Bluetooth")
                    bleServer?.sendText("Bluetooth")
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer, CircleShape).size(64.dp)
            ) {
                Icon(Icons.Filled.Bluetooth, contentDescription = "Bluetooth", tint = MaterialTheme.colorScheme.onSecondaryContainer)
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        showMessage("GiroIzq")
                        bleServer?.sendText("GiroIzq")
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer, CircleShape).size(64.dp)
                ) {
                    Icon(Icons.Filled.Undo, contentDescription = "GiroIzq", tint = MaterialTheme.colorScheme.onSecondaryContainer)
                }
                IconButton(
                    onClick = {
                        showMessage("GiroDer")
                        bleServer?.sendText("GiroDer")
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer, CircleShape).size(64.dp)
                ) {
                    Icon(Icons.Filled.Redo, contentDescription = "GiroDer", tint = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }
        }

        // Columna centro (Power, Stop, Luces)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxHeight()
        ) {
            IconButton(
                onClick = {
                    showMessage("Power")
                    bleServer?.sendText("Power")
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer, CircleShape).size(64.dp)
            ) {
                Icon(Icons.Filled.PowerSettingsNew, contentDescription = "Power", tint = MaterialTheme.colorScheme.onErrorContainer)
            }

            IconButton(
                onClick = {
                    showMessage("Stop")
                    bleServer?.sendText("Stop")
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer, CircleShape).size(64.dp)
            ) {
                Icon(Icons.Filled.Stop, contentDescription = "Stop", tint = MaterialTheme.colorScheme.onErrorContainer)
            }

            FilledTonalButton(
                onClick = {
                    showMessage("Luces")
                    bleServer?.sendText("Luces")
                },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                elevation = buttonElevation,
                modifier = Modifier.width(120.dp)
            ) {
                Icon(Icons.Filled.Lightbulb, contentDescription = "Luces", modifier = Modifier.padding(end = 8.dp))
                Text("Luces")
            }
        }

        // Columna derecha (WiFi, Arriba/Abajo)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxHeight()
        ) {
            IconButton(
                onClick = {
                    showMessage("WiFi")
                    bleServer?.sendText("WiFi")
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape).size(64.dp)
            ) {
                Icon(Icons.Filled.Wifi, contentDescription = "WiFi", tint = MaterialTheme.colorScheme.onTertiaryContainer)
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        showMessage("Arriba")
                        bleServer?.sendText("Arriba")
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer, CircleShape).size(64.dp)
                ) {
                    Icon(Icons.Filled.ArrowUpward, contentDescription = "Arriba", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                IconButton(
                    onClick = {
                        showMessage("Abajo")
                        bleServer?.sendText("Abajo")
                    },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer, CircleShape).size(64.dp)
                ) {
                    Icon(Icons.Filled.ArrowDownward, contentDescription = "Abajo", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }
    }
}
