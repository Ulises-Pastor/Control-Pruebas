package com.example.control_remoto.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme

private val LightColors = lightColorScheme(
    primary = Color(0xFF9C88FF),           // Lavanda pastel
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD7CCFF), // Lavanda muy claro
    onPrimaryContainer = Color(0xFF3A1C6E), // Morado oscuro suave
    secondary = Color(0xFF6FD8B8),         // Verde menta pastel
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFBFF3E6), // Verde menta claro
    onSecondaryContainer = Color(0xFF054D37), // Verde oscuro suave
    error = Color(0xFFEF9A9A),             // Rojo pastel suave
    onError = Color(0xFF6B0000),
    background = Color(0xFFFDF6F0),        // Crema muy claro
    onBackground = Color(0xFF1B1B1B),      // Gris oscuro para texto
    surface = Color(0xFFFFFFFF),           // Blanco puro
    onSurface = Color(0xFF1B1B1B)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF7366FF),            // Lavanda pastel oscuro
    onPrimary = Color.White,
    primaryContainer = Color(0xFF5C4BCF),  // Morado medio
    onPrimaryContainer = Color(0xFFD7CCFF),// Lavanda claro
    secondary = Color(0xFF4AB79D),          // Verde menta oscuro
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF2E6E57),// Verde bosque oscuro
    onSecondaryContainer = Color(0xFFBFF3E6),// Verde menta claro
    error = Color(0xFFB71C1C),              // Rojo oscuro
    onError = Color(0xFFFFCDD2),
    background = Color(0xFF1B1B1B),         // Gris muy oscuro
    onBackground = Color(0xFFFDF6F0),       // Crema claro para texto
    surface = Color(0xFF2E2E2E),            // Gris oscuro
    onSurface = Color(0xFFFDF6F0)
)

@Composable
fun RemoteControlTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = androidx.compose.material3.Typography(),
        shapes = androidx.compose.material3.Shapes(),
        content = content
    )
}
