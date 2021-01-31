package com.example.android.hc05coonect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Button moveToASCII = (Button)findViewById(R.id.ready);
        moveToASCII.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpActivity.this,BluetoothModuleActivity.class);
                startActivity(intent);
                HelpActivity.this.finish();
            }
        });
    }
}
