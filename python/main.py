import bluetooth
import control
import time
from machine import Pin

# UUIDs del servicio y característica
SERVICE_UUID = bluetooth.UUID("6e400001-b5a3-f393-e0a9-e50e24dcca9e")
CHAR_UUID = bluetooth.UUID("6e400003-b5a3-f393-e0a9-e50e24dcca9e")

# Configuración BLE
ble = bluetooth.BLE()
ble.active(True)

# Variables de estado
conn_handle = None
char_handle = None
is_connected = False
is_scanning = False
last_operation_time = time.ticks_ms()

# Configuración
SCAN_TIMEOUT_MS = 10000  # 10 segundos
RECONNECT_DELAY = 5      # 5 segundos

# LED
led = Pin("LED", Pin.OUT) if hasattr(Pin, "LED") else Pin(2, Pin.OUT)

# Funciones
def safe_ble_operation(func, *args):
    try:
        return func(*args)
    except Exception as e:
        print(f"⚠️ Error en operación BLE: {e}")
        return None

def stop_scan():
    global is_scanning
    if is_scanning:
        safe_ble_operation(ble.gap_scan, None)
        is_scanning = False

def try_reconnect():
    global conn_handle, char_handle, is_connected, is_scanning, last_operation_time
    if is_scanning:
        return
    conn_handle = None
    char_handle = None
    is_connected = False
    print("🔄 Reintentando conexión BLE...")
    led.on()
    stop_scan()
    is_scanning = safe_ble_operation(ble.gap_scan, SCAN_TIMEOUT_MS, 30000, 30000, False)
    last_operation_time = time.ticks_ms()

def disconnect():
    global conn_handle, char_handle, is_connected
    if conn_handle is not None:
        safe_ble_operation(ble.gap_disconnect, conn_handle)
    conn_handle = None
    char_handle = None
    is_connected = False
    led.off()
    stop_scan()

def ble_irq(event, data):
    global conn_handle, char_handle, is_connected, is_scanning, last_operation_time
    try:
        if event == 5:  # _IRQ_SCAN_RESULT
            addr_type, addr, adv_type, rssi, adv_data = data
            services = decode_services(adv_data)
            if services and SERVICE_UUID in services:
                print(f"🔍 Dispositivo encontrado: {bytes(addr).hex()}, RSSI: {rssi} dBm")
                stop_scan()
                safe_ble_operation(ble.gap_connect, addr_type, addr)
        elif event == 7:  # _IRQ_PERIPHERAL_CONNECT
            conn_handle, _, _ = data
            is_connected = True
            is_scanning = False
            print("✅ Conectado. Buscando servicios...")
            led.off()
            safe_ble_operation(ble.gattc_discover_services, conn_handle)
            last_operation_time = time.ticks_ms()
        elif event == 8:  # _IRQ_PERIPHERAL_DISCONNECT
            print("🔌 Desconectado del servidor.")
            disconnect()
            time.sleep(RECONNECT_DELAY)
            try_reconnect()
        elif event == 9:  # _IRQ_GATTC_SERVICE_RESULT
            if conn_handle is not None and len(data) >= 3:
                _, start_handle, end_handle, uuid = data
                print(f"📘 Servicio encontrado: {uuid}")
                if uuid == SERVICE_UUID:
                    print(f"   Handles: {start_handle} - {end_handle}")
                    safe_ble_operation(ble.gattc_discover_characteristics, conn_handle, start_handle, end_handle)
                    last_operation_time = time.ticks_ms()
        elif event == 11:  # _IRQ_GATTC_CHARACTERISTIC_RESULT
            if conn_handle is not None and len(data) >= 4:
                _, decl_handle, value_handle, properties, uuid = data
                print(f"🔧 Característica encontrada: {uuid}")
                print(f"   Handle: {value_handle}, Propiedades: {properties}")
                if uuid == CHAR_UUID:
                    char_handle = value_handle
                    print("📤 Configurando notificaciones...")
                    # Escribe en el descriptor CCCD
                    if safe_ble_operation(ble.gattc_write, conn_handle, value_handle + 1, b'\x01\x00', 1):
                        # Envía mensaje inicial
                        safe_ble_operation(ble.gattc_write, conn_handle, value_handle, b'Hola desde cliente', 1)
                    last_operation_time = time.ticks_ms()
        elif event == 18:  # _IRQ_GATTC_NOTIFY
            print("hola")
            if conn_handle is not None and char_handle is not None and len(data) >= 2:
                conn, value_handle, notify_data = data
                if conn == conn_handle and value_handle == char_handle:
                    process_notification(notify_data)
                    last_operation_time = time.ticks_ms()
    except Exception as e:
        print(f"⚠️ Error crítico en manejador BLE: {e}")
        disconnect()
        time.sleep(RECONNECT_DELAY)
        try_reconnect()

def process_notification(data):
    try:
        msg = bytes(data).decode('utf-8').strip()
        print(f"📨 Notificación recibida: '{msg}'")
        if msg == "Arriba":
            control.avanzar()
        elif msg == "Abajo":
            control.retroceder()
        elif msg == "Stop":
            control.detener()
        elif msg == "GiroIzq":
            control.girar_izquierda()
        elif msg == "GiroDer":
            control.girar_derecha()
        else:
            print("❓ Comando no reconocido")
    except Exception as e:
        print(f"⚠️ Error procesando notificación: {e}")

def decode_services(adv_data):
    services = []
    i = 0
    while i < len(adv_data):
        length = adv_data[i]
        if length == 0:
            break
        type = adv_data[i + 1]
        if type in [0x06, 0x07]:  # UUIDs de 16/128 bits
            uuid_bytes = adv_data[i+2:i+2+length-1]
            try:
                uuid = bluetooth.UUID(uuid_bytes)
                services.append(uuid)
            except:
                pass
        i += 1 + length
    return services

# Configuración inicial
ble.irq(ble_irq)
print("🚀 Iniciando cliente BLE...")
try_reconnect()

# Bucle principal
while True:
    try:
        # Verifica timeout de operaciones
        if time.ticks_diff(time.ticks_ms(), last_operation_time) > 15000:  # 15 segundos
            print("⏳ Timeout de operación. Reiniciando...")
            disconnect()
            time.sleep(RECONNECT_DELAY)
            try_reconnect()
        time.sleep(1)
    except KeyboardInterrupt:
        print("\n🛑 Deteniendo cliente BLE...")
        disconnect()
        ble.active(False)
        break
    except Exception as e:
        print(f"⚠️ Error en bucle principal: {e}")
        time.sleep(5)