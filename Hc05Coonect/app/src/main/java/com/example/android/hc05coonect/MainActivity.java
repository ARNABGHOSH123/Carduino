package com.example.android.hc05coonect;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.android.hc05coonect.R.id.Backward;



public class MainActivity extends AppCompatActivity {
    private EditText forward, back, right, left, stop;
    private TextView errFor,errBack,errRight,errLeft,errStop;
    private AlertDialog operateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ASCII Commands");
        setContentView(R.layout.activity_main);
        /**OperateThread ot = new OperateThread();
        ot.start();**/
        forward = (EditText) findViewById(R.id.Forward);
        back = (EditText) findViewById(Backward);
        right = (EditText) findViewById(R.id.Right);
        left = (EditText) findViewById(R.id.Left);
        stop = (EditText) findViewById(R.id.Stop);
        errFor = (TextView)findViewById(R.id.error_forward);
        errBack = (TextView)findViewById(R.id.error_backward);
        errRight = (TextView)findViewById(R.id.error_right);
        errLeft = (TextView)findViewById(R.id.error_left);
        errStop = (TextView)findViewById(R.id.error_stop);
        moveToOperate();
    }
    public void moveToOperate() {
        final Button operate = (Button)findViewById(R.id.operate);
        operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String f=forward.getText().toString(), b = back.getText().toString(), r = right.getText().toString(),
                        l = left.getText().toString(), s = stop.getText().toString();

                if (!f.equals("") && !b.equals("") && !r.equals("") && !l.equals("") && !s.equals("")) {

                    errFor.setVisibility(View.GONE);
                    errBack.setVisibility(View.GONE);
                    errRight.setVisibility(View.GONE);
                    errLeft.setVisibility(View.GONE);
                    errStop.setVisibility(View.GONE);
                    if(f.length()==1 && b.length()==1 && r.length()==1
                            && l.length()==1 && s.length()==1) {

                        RobotCommands.manualForward = f;
                        RobotCommands.manualBackward = b;
                        RobotCommands.manualRight = r;
                        RobotCommands.manualLeft = l;
                        RobotCommands.manualStop = s;


                        final CharSequence[] items = {"Manual mode", "Voice command mode","Sensing mode"};
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);


                        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                switch (i) {
                                    case 0: {
                                        Intent moToManualMode = new Intent(getApplicationContext(), ManualActivity.class);
                                        startActivity(moToManualMode);
                                        break;
                                    }
                                    case 1: {
                                        Intent moToVoiceMode = new Intent(getApplicationContext(), VoiceActivity.class);
                                        startActivity(moToVoiceMode);
                                        break;
                                    }
                                    case 2: {
                                        Intent moToSensingMode = new Intent(getApplicationContext(), SensingModeActivity.class);
                                        startActivity(moToSensingMode);
                                        break;
                                    }
                                    default:
                                        throw new IllegalArgumentException("Wrong choice!!");
                                }
                            }

                        });

                        operateDialog = builder.create();
                        //operateDialog.setTitle("Choose option to select mode");
                        operateDialog.setTitle(Html.fromHtml("<b>"+"Choose option to select mode"+"</b>"));
                        operateDialog.show();
                    }

                    if(f.length()>1)
                    {
                        errFor.setVisibility(View.VISIBLE);
                        errFor.setText(getResources().getString(R.string.error));
                    }
                    if(b.length()>1)
                    {
                        errBack.setVisibility(View.VISIBLE);
                        errBack.setText(getResources().getString(R.string.error));
                    }
                    if(r.length()>1)
                    {
                        errRight.setVisibility(View.VISIBLE);
                        errRight.setText(getResources().getString(R.string.error));
                    }
                    if(l.length()>1)
                    {
                        errLeft.setVisibility(View.VISIBLE);
                        errLeft.setText(getResources().getString(R.string.error));
                    }
                    if(s.length()>1)
                    {
                        errStop.setVisibility(View.VISIBLE);
                        errStop.setText(getResources().getString(R.string.error));
                    }

                } else{
                    Toast.makeText(getApplicationContext(), "Enter all * mark fields before proceeding!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        Menu submenu = menu.getItem(0).getSubMenu();
        submenu.clear();
        submenu.add(0,1,Menu.NONE,"Arduino cloud");
        submenu.add(0,2,Menu.NONE,"Google cloud service");
        submenu.add(0,3,Menu.NONE,"IOT framework");

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case 1:
                Intent intent1 = new Intent(Intent.ACTION_WEB_SEARCH);
                intent1.putExtra(SearchManager.QUERY,"https://cloud.arduino.cc");
                startActivity(intent1);
                return true;

            case 2:
                Intent intent2 = new Intent(Intent.ACTION_WEB_SEARCH);
                intent2.putExtra(SearchManager.QUERY,"https://cloud.google.com");
                startActivity(intent2);
                return true;

            case 3:
                Intent intent3 = new Intent(Intent.ACTION_WEB_SEARCH);
                intent3.putExtra(SearchManager.QUERY,"https://cloud.google.com/solutions/iot/");
                startActivity(intent3);
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(operateDialog!=null)
        {
            operateDialog.dismiss();
        }
    }
}
