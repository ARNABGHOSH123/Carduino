package com.example.android.hc05coonect;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SensingModeActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float x,y,z;
    private boolean stop = true;
    private Button start_stop;
    final BluetoothModuleActivity.ConnectedThread mConnectedThread = BluetoothModuleActivity.getInstance().new ConnectedThread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensing_mode);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(senSensorManager!=null) {
            senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else{finish();}
        start_stop = (Button)findViewById(R.id.button_start_stop);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if(mySensor.getType()==Sensor.TYPE_ACCELEROMETER)
        {
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
            Log.i("Sensing On: ", "x = " + (int) x + " y = " + (int) y + " z = " + (int) z);
            if(start_stop!=null) {
                start_stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String text = start_stop.getText().toString().trim();
                        if (text.equalsIgnoreCase(getResources().getString(R.string.motion_stop))) {
                            mConnectedThread.write(RobotCommands.manualStop);
                            start_stop.setText(getResources().getString(R.string.motion_start));
                            stop = true;
                        }
                        else
                        {
                            stop = false;
                            start_stop.setText(getResources().getString(R.string.motion_stop));
                        }
                    }
                });
            }
            if(!stop)
            moveViaSensingMode();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    private void moveViaSensingMode()
    {
        //start_stop.setText(getResources().getString(R.string.motion_stop));
        if (x < 0)
            mConnectedThread.write(RobotCommands.manualForward);
        else if (y < 0)
            mConnectedThread.write(RobotCommands.manualLeft);
        else if ((int)y==0 && x!=0 && z!=0)
            mConnectedThread.write(RobotCommands.manualBackward);
        else if ((int)x==0 && y!=0 && z!=0)
            mConnectedThread.write(RobotCommands.manualRight);
    }

}