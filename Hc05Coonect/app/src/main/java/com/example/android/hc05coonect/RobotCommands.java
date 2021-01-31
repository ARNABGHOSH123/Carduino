package com.example.android.hc05coonect;


import android.bluetooth.BluetoothSocket;

abstract class RobotCommands {
    //bot Specifications from Arduino Uno Code
    public static String botName;
    public static String manualForward;
    public static String manualBackward;
    public static String manualRight;
    public static String manualLeft;
    public static String manualStop;


    //listener mode commands
    public static String listenerForward = "forward";
    public static String listenerBackward = "backward";
    public static String listenerLeft = "left";
    public static String listenerRight = "right";
    public static String ListenerStop = "stop";

    //bluetooth connectivity and count of times voice activity is opened
    public static BluetoothSocket mmSocket;
    public static int count_voice=0;
}
