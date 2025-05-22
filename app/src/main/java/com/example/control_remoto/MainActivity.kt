package com.example.control_remoto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.control_remoto.ui.RemoteControlUI
import com.example.control_remoto.ui.theme.RemoteControlTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RemoteControlTheme {
                RemoteControlUI()
            }
        }
    }
}
