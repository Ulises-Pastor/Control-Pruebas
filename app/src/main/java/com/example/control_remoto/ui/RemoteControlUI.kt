package com.example.control_remoto.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip

@Composable
fun RemoteControlUI(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val buttonElevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)

    fun showMessage(message: String) {
        android.widget.Toast.makeText(context, "Acción: $message", android.widget.Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Botón de encendido
        FilledTonalButton(
            onClick = { showMessage("Encendido") },
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            elevation = buttonElevation,
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.PowerSettingsNew,
                contentDescription = "Power",
                modifier = Modifier.size(36.dp))
        }

        // Botones Bluetooth y WiFi
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { showMessage("Bluetooth") },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    )
                    .size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Bluetooth,
                    contentDescription = "Bluetooth",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(32.dp))
            }

            IconButton(
                onClick = { showMessage("WiFi") },
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = CircleShape
                    )
                    .size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Wifi,
                    contentDescription = "WiFi",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(32.dp))
            }
        }

        // Panel de control direccional
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 4.dp,
            modifier = Modifier.size(200.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Flecha superior
                IconButton(
                    onClick = { showMessage("Arriba") },
                    modifier = Modifier.align(Alignment.TopCenter)
                ) {
                    Icon(
                        Icons.Filled.ArrowUpward,
                        contentDescription = "Arriba",
                        tint = MaterialTheme.colorScheme.primary)
                }

                // Flecha izquierda
                IconButton(
                    onClick = { showMessage("Izquierda") },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Izquierda",
                        tint = MaterialTheme.colorScheme.primary)
                }

                // Botón central
                IconButton(
                    onClick = { showMessage("Centro") },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Filled.Stop,
                        contentDescription = "Centro",
                        tint = MaterialTheme.colorScheme.error)
                }

                // Flecha derecha
                IconButton(
                    onClick = { showMessage("Derecha") },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Derecha",
                        tint = MaterialTheme.colorScheme.primary)
                }

                // Flecha inferior
                IconButton(
                    onClick = { showMessage("Abajo") },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Icon(
                        Icons.Filled.ArrowDownward,
                        contentDescription = "Abajo",
                        tint = MaterialTheme.colorScheme.primary)
                }
            }
        }

        // Botón de luces
        FilledTonalButton(
            onClick = { showMessage("Luces") },
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            elevation = buttonElevation,
            modifier = Modifier.width(120.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Lightbulb,
                contentDescription = "Luces",
                modifier = Modifier.padding(end = 8.dp))
            Text("Luces")
        }
    }
}
