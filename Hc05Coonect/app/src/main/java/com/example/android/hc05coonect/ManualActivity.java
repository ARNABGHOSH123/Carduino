package com.example.android.hc05coonect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Set;
import static com.example.android.hc05coonect.RobotCommands.mmSocket;


public class ManualActivity extends AppCompatActivity implements AccelerometerListener{
    BluetoothAdapter mAdapter;
    BluetoothDevice device;
    private ImageView forward,backward,right,left;
    private TextToSpeech tts;
    private AlertDialog.Builder wait ;
    private AlertDialog builder;
    private Handler hand;
    final BluetoothModuleActivity.ConnectedThread mConnectedThread = BluetoothModuleActivity.getInstance().new ConnectedThread();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        mAdapter = BluetoothAdapter.getDefaultAdapter();


        wait = new AlertDialog.Builder(this);
        wait.setView(R.layout.manual_mode_enter);
        builder = wait.create();
        builder.setCancelable(false);
        builder.show();
        hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                builder.dismiss();
                Toast.makeText(getApplicationContext(), "Manual mode configured", Toast.LENGTH_SHORT).show();
            }
        }, 5000);


        forward = (ImageView) findViewById(R.id.up);
        backward = (ImageView) findViewById(R.id.down);
        right = (ImageView) findViewById(R.id.right);
        left = (ImageView) findViewById(R.id.left);

        forward.setVisibility(View.INVISIBLE);
        backward.setVisibility(View.INVISIBLE);
        right.setVisibility(View.INVISIBLE);
        left.setVisibility(View.INVISIBLE);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS)
                {
                    if(tts.isLanguageAvailable(Locale.getDefault())==TextToSpeech.LANG_AVAILABLE)
                        tts.setLanguage(Locale.getDefault());
                }
                else if(i==TextToSpeech.ERROR)
                {
                    Toast.makeText(getApplicationContext(), "Cannot speak the text", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Set<BluetoothDevice> pairedModule = mAdapter.getBondedDevices();
        for(BluetoothDevice devv: pairedModule)
        {
            if(devv.getBondState()==BluetoothDevice.BOND_BONDED) {
                device = devv;
                Log.d("Connected devices: ","Connected device is: "+device);

                try {
                    OutputStream outputStream = mmSocket.getOutputStream();
                    Log.d("mConnected Thread is: ",mConnectedThread+"");

                    final Button motion = (Button)findViewById(R.id.motion);
                    motion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String mot = motion.getText().toString();
                            Log.i("Check button status: ",mot);
                            if(mot.equalsIgnoreCase(getResources().getString(R.string.motion_stop)))
                            {
                                speakCommand("STOP");
                                        mConnectedThread.write(RobotCommands.manualStop);
                                motion.setText(getResources().getString(R.string.motion_start));
                                forward.setVisibility(View.INVISIBLE);
                                backward.setVisibility(View.INVISIBLE);
                                right.setVisibility(View.INVISIBLE);
                                left.setVisibility(View.INVISIBLE);

                            }
                            else
                            {
                                speakCommand("Bot ready");
                                motion.setText(getResources().getString(R.string.motion_stop));

                                forward.setVisibility(View.VISIBLE);
                                backward.setVisibility(View.VISIBLE);
                                right.setVisibility(View.VISIBLE);
                                left.setVisibility(View.VISIBLE);

                                forward.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        speakCommand("Move forward");
                                                Log.i("FF: ", RobotCommands.manualForward);
                                                mConnectedThread.write(RobotCommands.manualForward);

                                    }
                                });

                                backward.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        speakCommand("move backward");
                                                Log.i("BB: ",RobotCommands.manualBackward);
                                                mConnectedThread.write(RobotCommands.manualBackward);

                                    }
                                });

                                right.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        speakCommand("Move right");
                                                Log.i("RR: ",RobotCommands.manualRight);
                                                mConnectedThread.write(RobotCommands.manualRight);
                                    }
                                });

                                left.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        speakCommand("Move left");
                                                Log.i("LL: ",RobotCommands.manualLeft);
                                                mConnectedThread.write(RobotCommands.manualLeft);

                                    }
                                });
                            }
                        }
                    });
                }
                catch (IOException ex)
                {
                    Toast.makeText(ManualActivity.this,"Invalid connection",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sendsettings,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_control_settings: {
                Intent cont = new Intent(getApplicationContext(), ASCII_For_Manual.class);
                startActivity(cont);
                return true;
            }
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void speakCommand(String command)
    {
        if(tts.isSpeaking())
        tts.stop();
        tts.speak(command,TextToSpeech.QUEUE_FLUSH,null);
    }
    @Override
    protected void onPause() {
        tts.stop();
        tts.shutdown();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AccelerometerManager.isSupported(this)) {
            AccelerometerManager.startListening(this);
        }
    }

    @Override
    public void onAccelerationChanged(float x, float y, float z) {

    }

    @Override
    public void onShake(float force) {
        Toast.makeText(this, "Emergency breaks applied", Toast.LENGTH_SHORT).show();
        speakCommand("Emergency breaks applied. Bot stopped.");
        mConnectedThread.write(RobotCommands.manualStop);
    }

    @Override
    public void onStop() {
        super.onStop();

//Check device supported Accelerometer senssor or not
        if (AccelerometerManager.isListening()) {

//Start Accelerometer Listening
            AccelerometerManager.stopListening();

            Toast.makeText(this, "Accelerometer Stopped", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (AccelerometerManager.isListening()) {
            AccelerometerManager.stopListening();

            Toast.makeText(this, "Accelerometer Stopped", Toast.LENGTH_SHORT).show();
        }
    }
}