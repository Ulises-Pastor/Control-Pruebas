from machine import Pin,PWM
from time import sleep

# Pines de los motores
in1 = Pin(12, Pin.OUT)
in2 = Pin(13, Pin.OUT)
in3 = Pin(26, Pin.OUT)
in4 = Pin(27, Pin.OUT)
ena = PWM(Pin(14), freq=10000)  # Velocidad motor A
enb = PWM(Pin(25), freq=10000)  # Velocidad motor B

# Duty cycle entre 0 (0%) y 1023 (100%)
def set_velocidad_mov(duty):
    ena.duty(duty)

def set_velocidad_giro(duty):
    enb.duty(duty)

def avanzar():
    in1.on(); in2.off()
    set_velocidad_mov(800)  # Ajusta aquí la velocidad

def retroceder():
    in1.off(); in2.on()
    set_velocidad_mov(800)

def detener():
    in1.off(); in2.off()
    set_velocidad_mov(0)
    
def girar_izquierda():
    in3.on(); in4.off()
    set_velocidad_giro(1000)
    
def girar_derecha():
    in3.off(); in4.on()
    set_velocidad_giro(1000)

def centrar():
    in3.off(); in4.off()
