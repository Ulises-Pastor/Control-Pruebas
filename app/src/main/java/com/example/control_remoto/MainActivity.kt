package com.example.control_remoto

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import com.example.control_remoto.ui.RemoteControlUI
import com.example.control_remoto.ui.theme.RemoteControlTheme

class MainActivity : ComponentActivity() {
    lateinit var bleServer: BleServerService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bleServer = BleServerService(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
            bleServer.start()
        }

        setContent {
            RemoteControlTheme {
                RemoteControlUI()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            bleServer.start()
        } else {
            Log.e("BLE_SERVER", "❌ Permisos requeridos denegados")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bleServer.stop()
    }
}
