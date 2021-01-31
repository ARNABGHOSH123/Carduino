package com.example.android.hc05coonect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ASCII_For_Manual extends AppCompatActivity {
    private TextView forw,back,right,left,stop;
    private Button changesett;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ascii_for_manual);
        forw = (TextView)findViewById(R.id.show_forward);
        back = (TextView)findViewById(R.id.show_backward);
        right = (TextView)findViewById(R.id.show_right);
        left = (TextView)findViewById(R.id.show_left);
        stop = (TextView)findViewById(R.id.show_stop);
        changesett = (Button)findViewById(R.id.change_settings);
        show_Datas();
        moveToMain();
    }
    public void show_Datas()
    {
        forw.setText("\t\t'"+RobotCommands.manualForward+"'");
        back.setText("\t\t'"+RobotCommands.manualBackward+"'");
        right.setText("\t\t'"+RobotCommands.manualRight+"'");
        left.setText("\t\t'"+RobotCommands.manualLeft+"'");
        stop.setText("\t\t'"+RobotCommands.manualStop+"'");
    }
    public void moveToMain()
    {
        changesett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent move = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(move);
                ASCII_For_Manual.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ASCII_For_Manual.this.finish();
    }
}
