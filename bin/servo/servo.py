import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BOARD)
GPIO.setup(7, GPIO.OUT)
SERVO = GPIO.PWM(7, 50)
SERVO.start(0)

SERVO.ChangeDutyCycle(2)
time.sleep(1.5)
SERVO.ChangeDutyCycle(1)
time.sleep(1.5)

GPIO.cleanup()
