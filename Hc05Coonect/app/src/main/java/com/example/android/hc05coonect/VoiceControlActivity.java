package com.example.android.hc05coonect;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceControlActivity extends AppCompatActivity {

    private EditText stringForward,stringBackward,stringLeft,stringRight,stringStop;
    private Button moveToVoiceMode;
    private TextView errStringFor,errStringBack,errStringRight,errStringLeft,errStringStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);
        stringForward = (EditText)findViewById(R.id.Forward);
        stringBackward = (EditText)findViewById(R.id.Backward);
        stringLeft = (EditText)findViewById(R.id.Left);
        stringRight = (EditText)findViewById(R.id.Right);
        stringStop = (EditText)findViewById(R.id.Stop);
        moveToVoiceMode = (Button)findViewById(R.id.save_proceed);
        errStringFor = (TextView)findViewById(R.id.error_forward);
        errStringBack = (TextView)findViewById(R.id.error_backward);
        errStringRight = (TextView)findViewById(R.id.error_right);
        errStringLeft = (TextView)findViewById(R.id.error_left);
        errStringStop = (TextView)findViewById(R.id.error_stop);
        moveToVoice();
    }
    public void moveToVoice()
    {
        moveToVoiceMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String f=stringForward.getText().toString(), b = stringBackward.getText().toString(), r = stringRight.getText().toString(),
                        l = stringLeft.getText().toString(), s = stringStop.getText().toString();
                errStringFor.setVisibility(View.GONE);
                errStringStop.setVisibility(View.GONE);
                errStringLeft.setVisibility(View.GONE);
                errStringRight.setVisibility(View.GONE);
                errStringBack.setVisibility(View.GONE);

                if(!f.equals("") && !b.equals("") && !r.equals("") && !l.equals("") && !s.equals(""))
                {
                    final AlertDialog.Builder builder=new AlertDialog.Builder(VoiceControlActivity.this);
                    builder.setMessage("Are you sure you want to save changes and proceed?").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            RobotCommands.listenerForward = f;
                            RobotCommands.listenerBackward = b;
                            RobotCommands.listenerLeft = l;
                            RobotCommands.listenerRight = r;
                            RobotCommands.ListenerStop = s;
                            Intent intent= new Intent(VoiceControlActivity.this,VoiceActivity.class);
                            startActivity(intent);
                            VoiceControlActivity.this.finish();
                            Toast.makeText(getApplicationContext(),"Changes saved successfully",Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                        }
                    });
                    AlertDialog save = builder.create();
                    save.setTitle(Html.fromHtml("<b>"+"Save and Proceed!"+"</b>"));
                    save.show();
                }
                if(f.equals(""))
                {
                    errStringFor.setVisibility(View.VISIBLE);
                    errStringFor.setMaxLines(1);
                    errStringFor.setText(getResources().getString(R.string.field_empty));
                }
                if(b.equals(""))
                {
                    errStringBack.setVisibility(View.VISIBLE);
                    errStringBack.setMaxLines(1);
                    errStringBack.setText(getResources().getString(R.string.field_empty));
                }
                if(r.equals(""))
                {
                    errStringRight.setVisibility(View.VISIBLE);
                    errStringRight.setMaxLines(1);
                    errStringRight.setText(getResources().getString(R.string.field_empty));
                }
                if(l.equals(""))
                {
                    errStringLeft.setVisibility(View.VISIBLE);
                    errStringLeft.setMaxLines(1);
                    errStringLeft.setText(getResources().getString(R.string.field_empty));
                }
                if(s.equals(""))
                {
                    errStringStop.setVisibility(View.VISIBLE);
                    errStringStop.setMaxLines(1);
                    errStringStop.setText(getResources().getString(R.string.field_empty));
                }
            }
        });
    }


}
