package com.example.android.hc05coonect;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import static android.R.attr.x;

public class StartUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setResult(Activity.RESULT_CANCELED);
        setContentView(R.layout.activity_start_up);
        final ProgressBar p = (ProgressBar)findViewById(R.id.progress_status);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),BluetoothModuleActivity.class);
                startActivity(intent);
                StartUpActivity.this.finish();
            }
        },3500);


    }
}
