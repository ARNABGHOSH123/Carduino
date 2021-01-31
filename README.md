# CarduinoCarController
Android app controlled robot build from scratch starting from Bluetooth listener to sending byte commands using Bluetooth V2.0 protocol
and controlling the robot via speaking or tapping mode or accelerometer mode(gaming mode).

#Speaking mode
"Right","Left","Forward","Backward" are the keywords(inputs) for Google TTS Engine which detects it and sends the String decoded message as bytes to the bluetooth HC05 module.

#Tapping mode
Four buttons each for left, right, forward, backward are present on the screen and tapping on them changes the direction of the vehicle as and when needed.

#Accelerometer mode
Tilting the mobile device towards left, right, forward and backward direction changes the vehicle direction (like a steering wheel).

# Application Stack
Arduino Microcontroller,Android Studio(Java Codebase),Bluetooth V2.0 Data Transfer Protocol.

Changes Scope:- The device needs to have GPS decoding and location tracking properties. It is made as a blueprint of controlling devices on remote areas anywhere requiring large
bandwidth protocols for communication.

Short Demonstration video (Only accelerometer mode was recorded):- https://youtu.be/OBDYQUxNG8o
