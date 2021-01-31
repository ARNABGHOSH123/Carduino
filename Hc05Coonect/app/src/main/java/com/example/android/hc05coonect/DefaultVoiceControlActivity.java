package com.example.android.hc05coonect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DefaultVoiceControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_voice_control);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DefaultVoiceControlActivity.this.finish();
    }
}
