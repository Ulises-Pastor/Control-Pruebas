package com.example.control_remoto

import android.Manifest
import android.bluetooth.*
import android.bluetooth.le.*
import android.content.Context
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import androidx.core.app.ActivityCompat
import java.util.*

class BleServerService(private val context: Context) {
    private val SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e")
    private val CHARACTERISTIC_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e")

    private val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val adapter = manager.adapter
    private val advertiser = adapter.bluetoothLeAdvertiser
    private var gattServer: BluetoothGattServer? = null

    private lateinit var characteristic: BluetoothGattCharacteristic
    private var lastDevice: BluetoothDevice? = null
    private val handler = Handler(Looper.getMainLooper())

    fun start() {
        if (!adapter.isEnabled) {
            Log.e("BLE_SERVER", "❌ Bluetooth no está habilitado")
            return
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BLE_SERVER", "❌ Permiso BLUETOOTH_CONNECT no concedido")
            return
        }

        gattServer = manager.openGattServer(context, gattCallback)

        val service = BluetoothGattService(SERVICE_UUID, BluetoothGattService.SERVICE_TYPE_PRIMARY)

        characteristic = BluetoothGattCharacteristic(
            CHARACTERISTIC_UUID,
            BluetoothGattCharacteristic.PROPERTY_READ or
                    BluetoothGattCharacteristic.PROPERTY_WRITE or
                    BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            BluetoothGattCharacteristic.PERMISSION_READ or
                    BluetoothGattCharacteristic.PERMISSION_WRITE
        )

        service.addCharacteristic(characteristic)
        gattServer?.addService(service)

        startAdvertising()
    }

    private fun startAdvertising() {
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setConnectable(true)
            .build()

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .addServiceUuid(ParcelUuid(SERVICE_UUID))
            .build()

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BLE_SERVER", "❌ Permiso BLUETOOTH_ADVERTISE no concedido")
            return
        }

        advertiser.startAdvertising(settings, data, advertiseCallback)
    }

    private fun restartAdvertising() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) return

        Log.i("BLE_SERVER", "🔁 Reiniciando advertising BLE...")
        advertiser.stopAdvertising(advertiseCallback)
        handler.postDelayed({
            startAdvertising()
        }, 500)
    }

    private fun startAutoPing() {
        handler.post(object : Runnable {
            override fun run() {
                if (lastDevice != null) {
                    sendText("ping")
                    handler.postDelayed(this, 2000) // cada 2 segundos
                }
            }
        })
    }

    private val advertiseCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            Log.i("BLE_SERVER", "✅ Advertising iniciado")
        }

        override fun onStartFailure(errorCode: Int) {
            Log.e("BLE_SERVER", "❌ Error al iniciar advertising: $errorCode")
        }
    }

    private val gattCallback = object : BluetoothGattServerCallback() {
        override fun onConnectionStateChange(device: BluetoothDevice, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("BLE_SERVER", "📲 Dispositivo conectado: ${device.address}")
                lastDevice = device
                startAutoPing()  // Enviar ping continuo al conectar
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("BLE_SERVER", "🔌 Dispositivo desconectado: ${device.address}")
                if (lastDevice?.address == device.address) {
                    lastDevice = null
                }
                restartAdvertising()
            }
        }
    }

    fun sendText(text: String) {
        if (lastDevice == null) {
            Log.w("BLE_SERVER", "⚠️ No hay dispositivo conectado para enviar: $text")
            return
        }

        val value = text.toByteArray(Charsets.UTF_8)
        characteristic.value = value

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BLE_SERVER", "❌ Permiso BLUETOOTH_CONNECT no concedido para notificación")
            return
        }

        val result = gattServer?.notifyCharacteristicChanged(lastDevice, characteristic, false)
        Log.d("BLE_SERVER", "📤 Enviado '$text' con resultado: $result")
    }

    fun stop() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) return
        advertiser.stopAdvertising(advertiseCallback)
        gattServer?.close()
        gattServer = null
        lastDevice = null
    }
}
