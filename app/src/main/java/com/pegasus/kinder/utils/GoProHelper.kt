package com.pegasus.kinder.utils

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.*
import java.util.UUID

class GoProHelper(private val context: Context, private val scope: CoroutineScope) {
    companion object {
        private const val TAG = "GoProHelper"
        private const val GOPRO_NAME_PREFIX = "GoPro"
        
        // UUIDs for GoPro
        private const val GOPRO_SERVICE_UUID = "0000fea6-0000-1000-8000-00805f9b34fb"
        private const val CONTROL_CHAR_UUID = "b5f90072-aa8d-11e3-9046-0002a5d5c51b"
        
        // Updated commands with proper command structure for HERO9+
        private val COMMAND_SET_VIDEO_MODE = byteArrayOf(0x06, 0x40, 0x02, 0x01, 0x00, 0x00)
        private val COMMAND_START_RECORD = byteArrayOf(0x03, 0x01, 0x01, 0x01, 0x00)
        private val COMMAND_STOP_RECORD = byteArrayOf(0x03, 0x01, 0x01, 0x00, 0x00)
        private val COMMAND_KEEP_ALIVE = byteArrayOf(0x03, 0x16, 0x01, 0x00, 0x00)
    }

    private var bluetoothGatt: BluetoothGatt? = null
    private var commandCharacteristic: BluetoothGattCharacteristic? = null
    private var isConnected = false
    private var keepAliveJob: Job? = null
    var isRecording = false
        private set

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.d(TAG, "Connected to GATT server.")
                    isConnected = true
                    gatt.discoverServices()
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d(TAG, "Disconnected from GATT server.")
                    isConnected = false
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val service = gatt.getService(UUID.fromString(GOPRO_SERVICE_UUID))
                commandCharacteristic = service?.getCharacteristic(UUID.fromString(CONTROL_CHAR_UUID))
                Log.d(TAG, "Services discovered. Command characteristic found: ${commandCharacteristic != null}")
            } else {
                Log.e(TAG, "Service discovery failed with status: $status")
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            Log.d(TAG, "Characteristic write status: $status")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Command written successfully")
            } else {
                Log.e(TAG, "Command write failed")
            }
        }
    }

    suspend fun startCamera(onSuccess: () -> Unit, onError: (String) -> Unit) = withContext(Dispatchers.IO) {
        try {
            if (bluetoothAdapter == null) {
                onError("Bluetooth not supported on this device")
                return@withContext
            }

            if (!bluetoothAdapter!!.isEnabled) {
                onError("Please enable Bluetooth")
                return@withContext
            }

            // First try to connect to already paired device
            val pairedDevice = findPairedGoPro()
            if (pairedDevice != null) {
                Log.d(TAG, "Found paired GoPro device, attempting connection...")
                connectToDevice(pairedDevice)
                delay(2000) // Wait for connection
                
                if (isConnected) {
                    onSuccess()
                    return@withContext
                }
            }

            // If no paired device or connection failed, try scanning
            Log.d(TAG, "Starting BLE scan for GoPro...")
            val scanner = bluetoothAdapter!!.bluetoothLeScanner
            var deviceFound = false

            val scanCallback = object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    val deviceName = result.device.name
                    Log.d(TAG, "Found device: $deviceName")
                    
                    if (deviceName?.startsWith(GOPRO_NAME_PREFIX) == true) {
                        deviceFound = true
                        scanner.stopScan(this)
                        connectToDevice(result.device)
                    }
                }

                override fun onScanFailed(errorCode: Int) {
                    Log.e(TAG, "Scan failed with error: $errorCode")
                }
            }

            scanner.startScan(scanCallback)
            delay(10000) // Scan timeout
            scanner.stopScan(scanCallback)

            delay(2000) // Wait for potential connection

            if (!isConnected) {
                onError("Could not find or connect to GoPro device. Please ensure it's turned on and in pairing mode.")
            } else {
                onSuccess()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error starting camera", e)
            onError("Error starting camera: ${e.message}")
        }
    }

    private fun findPairedGoPro(): BluetoothDevice? {
        return bluetoothAdapter?.bondedDevices?.find { device ->
            device.name?.startsWith(GOPRO_NAME_PREFIX) == true
        }
    }

    private fun connectToDevice(device: BluetoothDevice) {
        try {
            Log.d(TAG, "Attempting to connect to device: ${device.name}")
            bluetoothGatt?.close() // Close any existing connection
            bluetoothGatt = device.connectGatt(context, false, gattCallback, BluetoothDevice.TRANSPORT_LE)
        } catch (e: Exception) {
            Log.e(TAG, "Error connecting to device", e)
        }
    }

    private suspend fun ensureConnection(): Boolean = withContext(Dispatchers.IO) {
        if (isConnected) return@withContext true
        
        var attempts = 0
        while (attempts < 3 && !isConnected) {
            try {
                // Attempt to reconnect
                bluetoothGatt?.connect()
                delay(2000)
                attempts++
            } catch (e: Exception) {
                Log.e(TAG, "Error reconnecting", e)
            }
        }
        
        isConnected
    }

    private fun sendCommand(command: ByteArray): Boolean {
        return try {
            Log.d(TAG, "Sending command: ${command.joinToString(", ") { "0x%02X".format(it) }}")
            commandCharacteristic?.let { characteristic ->
                characteristic.value = command
                characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                
                Thread.sleep(1500) // Increased delay before sending
                
                val success = bluetoothGatt?.writeCharacteristic(characteristic) == true
                Log.d(TAG, "Command sent successfully: $success")
                success
            } ?: run {
                Log.e(TAG, "Command characteristic is null")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending command", e)
            false
        }
    }

    suspend fun startRecording(onError: (String) -> Unit) = withContext(Dispatchers.IO) {
        if (isRecording) return@withContext
        
        try {
            Log.d(TAG, "Attempting to start recording...")
            
            if (!ensureConnection()) {
                Log.e(TAG, "Failed to ensure connection")
                onError("Failed to connect to GoPro")
                return@withContext
            }

            // First set to video mode
            Log.d(TAG, "Setting video mode...")
            if (!sendCommand(COMMAND_SET_VIDEO_MODE)) {
                Log.e(TAG, "Failed to set video mode")
                onError("Failed to set video mode")
                return@withContext
            }

            delay(3000) // Wait longer for mode to be set

            // Start recording with multiple attempts
            Log.d(TAG, "Sending start recording command...")
            var recordingStarted = false
            for (attempt in 1..3) {
                Log.d(TAG, "Start recording attempt $attempt")
                if (sendCommand(COMMAND_START_RECORD)) {
                    delay(4000) // Increased delay to wait for recording to start
                    recordingStarted = true
                    Log.d(TAG, "Start recording command sent successfully")
                    break
                }
                Log.d(TAG, "Start recording attempt $attempt failed, retrying...")
                delay(2000)
            }

            if (recordingStarted) {
                isRecording = true
                Log.d(TAG, "Recording started successfully")
                startKeepAlive()
            } else {
                Log.e(TAG, "Failed to start recording after multiple attempts")
                onError("Failed to start recording")
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error starting recording", e)
            onError("Error starting recording: ${e.message}")
        }
    }

    private fun startKeepAlive() {
        keepAliveJob?.cancel()
        keepAliveJob = scope.launch(Dispatchers.IO) {
            try {
                while (isRecording && isActive) {
                    Log.d(TAG, "Sending keep-alive command...")
                    if (!sendCommand(COMMAND_KEEP_ALIVE)) {
                        if (!ensureConnection()) {
                            Log.e(TAG, "Keep-alive failed and reconnection unsuccessful")
                            break
                        }
                    }
                    delay(2000)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in keep-alive loop", e)
            }
        }
    }

    suspend fun stopRecording() = withContext(Dispatchers.IO) {
        if (!isRecording) return@withContext
        
        try {
            Log.d(TAG, "Attempting to stop recording...")
            keepAliveJob?.cancel()
            
            if (!ensureConnection()) {
                Log.e(TAG, "Lost connection while trying to stop recording")
                return@withContext
            }

            if (sendCommand(COMMAND_STOP_RECORD)) {
                isRecording = false
                Log.d(TAG, "Recording stopped successfully")
            } else {
                Log.e(TAG, "Failed to send stop recording command")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping recording", e)
        }
    }

    suspend fun release() {
        try {
            keepAliveJob?.cancel()
            if (isRecording) {
                stopRecording()
            }
            bluetoothGatt?.close()
            bluetoothGatt = null
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing resources", e)
        }
    }
} 