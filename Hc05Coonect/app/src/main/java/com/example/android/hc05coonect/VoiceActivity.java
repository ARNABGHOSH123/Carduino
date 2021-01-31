package com.example.android.hc05coonect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import android.speech.tts.TextToSpeech;

public class VoiceActivity extends AppCompatActivity implements AccelerometerListener{
    private String command="";
    private final int REQ_CODE=100;
    private TextView showVoiceCommands;
    private EditText name_bot;
    BluetoothAdapter mAdapter;
    BluetoothDevice device;
    private AlertDialog.Builder wait ;
    private AlertDialog builder;
    private Handler hand;
    private TextToSpeech tts;
    final BluetoothModuleActivity.ConnectedThread mConnectedThread = BluetoothModuleActivity.getInstance().new ConnectedThread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        if(RobotCommands.count_voice==0) {
            wait = new AlertDialog.Builder(this);
            wait.setView(R.layout.voice_mode_enter);
            builder = wait.create();
            builder.setCancelable(false);
            builder.show();
            hand = new Handler();
            hand.postDelayed(new Runnable() {
                @Override
                public void run() {
                    builder.dismiss();
                    Toast.makeText(getApplicationContext(), "Voice mode configured", Toast.LENGTH_SHORT).show();
                }
            }, 5000);
            RobotCommands.count_voice = 1;
        }

        showVoiceCommands = (TextView) findViewById(R.id.show_commands);
        name_bot = (EditText) findViewById(R.id.Name);

        mAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedModule = mAdapter.getBondedDevices();
        for (BluetoothDevice devv : pairedModule) {
            if (devv.getBondState() == BluetoothDevice.BOND_BONDED) {
                device = devv;
                Log.d("Connected devices: ", "Connected device is: " + device);
                try
                {
                    final ImageView voicecommand = (ImageView)findViewById(R.id.send_command);
                    voicecommand.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            acceptName();

                            startVoiceInput();
                        }
                    });
                }
                catch (Exception ex){}


            }
        }

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
    }
    public void startVoiceInput()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if(RobotCommands.botName.equals(""))
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Bruno is hearing for you:) !!");
        else
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,RobotCommands.botName + " is hearing for you:) !!");
        try
        {
            startActivityForResult(intent,REQ_CODE);
        }
        catch(ActivityNotFoundException e)
        {
            Log.e("MainActivity","Couldn't start activity!!");
        }
    }
    public void acceptName()
    {
        String name_of_the_bot = name_bot.getText().toString();
        if(name_of_the_bot.equals(""))
        {
            RobotCommands.botName = "Bruno";
        }
        else{
            RobotCommands.botName = name_of_the_bot;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

       if(resultCode==RESULT_OK && requestCode==REQ_CODE)
       {
                if(data!=null)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    command = result.get(0);
                    Log.i("Command: ",command);
                    if(!command.equals("")) {
                        showVoiceCommands.setVisibility(View.VISIBLE);
                        showVoiceCommands.setText(command);
                    }
                    else
                    {
                        showVoiceCommands.setVisibility(View.GONE);
                    }

                    if(command.equalsIgnoreCase(RobotCommands.listenerForward))
                    {
                        Log.i("Movement: ",RobotCommands.manualForward);
                        mConnectedThread.write("F");
                    }
                    else if(command.equalsIgnoreCase(RobotCommands.listenerBackward))
                    {
                        Log.i("Movement: ",RobotCommands.manualBackward);
                        mConnectedThread.write(RobotCommands.manualBackward);
                    }
                    else if(command.equalsIgnoreCase(RobotCommands.listenerLeft))
                    {
                        Log.i("Movement: ",RobotCommands.manualLeft);
                        mConnectedThread.write(RobotCommands.manualLeft);
                    }
                    else if(command.equalsIgnoreCase(RobotCommands.listenerRight))
                    {
                        Log.i("Movement: ",RobotCommands.manualRight);
                        mConnectedThread.write(RobotCommands.manualRight);
                    }
                    else if(command.equalsIgnoreCase(RobotCommands.ListenerStop))
                    {
                        Log.i("Movement: ",RobotCommands.manualStop);
                        mConnectedThread.write(RobotCommands.manualStop);
                    }


                }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_voice,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_control:
                Intent intent = new Intent(getApplicationContext(),ASCII_For_Voice.class);
                startActivity(intent);
                return true;

            case R.id.voice_settings:
                Intent intent1 = new Intent(getApplicationContext(),VoiceControlActivity.class);
                startActivity(intent1);
                return true;

            case R.id.action_default:
                Intent defaults = new Intent(getApplicationContext(),DefaultVoiceControlActivity.class);
                startActivity(defaults);
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return  true;
        }
        return super.onOptionsItemSelected(item);
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
        speakCommand("Emergency breaks applied..");
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
    public void speakCommand(String command)
    {
        if(tts.isSpeaking())
            tts.stop();
        tts.speak(command,TextToSpeech.QUEUE_FLUSH,null);
    }
}