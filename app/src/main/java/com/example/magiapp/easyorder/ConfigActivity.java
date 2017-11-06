package com.example.magiapp.easyorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigActivity extends AppCompatActivity {
    Button connectButton;
    EditText ipField;
    TextView connectStatusTV;
    String ipVal;
    private SharedPreferences ipPreference;
    CheckBox cb_remember;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        connectButton = (Button) findViewById(R.id.b_connect);
        ipField = (EditText) findViewById(R.id.et_ipField);
        connectStatusTV = (TextView) findViewById(R.id.tv_connectStatus);
        cb_remember = (CheckBox) findViewById(R.id.cb_rememberIP);
        connectButton.setOnClickListener(new connectServer());
        ipPreference = getPreferences(MODE_PRIVATE);
        getPreference();

    }

    public class connectServer implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            ipVal = ipField.getText().toString();


            Toast.makeText(ConfigActivity.this, "Connected to server "+ ipVal , Toast.LENGTH_SHORT ).show();

            if(cb_remember.isChecked()) {
                putPreference();                                 //Save IP
            }
            else{
                ipPreference.edit().clear().apply();            //Clear saved IP
            }

            connectStatusTV.setText("Connected");
            Intent returnIntent = new Intent();
            String userResponseText = ipVal;
            returnIntent.putExtra("result", userResponseText);
            setResult(RESULT_OK, returnIntent);
            finish();


        }
    }

    private void putPreference(){
        String ip = ipField.getText().toString();
        Boolean isChecked = cb_remember.isChecked();

        SharedPreferences.Editor editor = ipPreference.edit();
        editor.putString("pref_ip",ip);
        editor.putBoolean("pref_save",isChecked);
        editor.apply();
    }

    private void getPreference(){
        if(ipPreference.contains("pref_ip"))
            ipField.setText(ipPreference.getString("pref_ip",""));
        if(ipPreference.contains("pref_save"))
            cb_remember.setChecked(ipPreference.getBoolean("pref_save", false));
    }











    /*

    @Override
    protected void onDestroy() {
        ipVal = ipField.getText().toString();
        //Toast.makeText(ConfigActivity.this, "Connected to server "+ ipVal, Toast.LENGTH_SHORT ).show();
        Intent returnIntent = new Intent();
        String userResponseText = "dkld";
        returnIntent.putExtra("result", "wtf");
        if (ipVal != null)
            setResult(RESULT_OK, returnIntent);
        else
            setResult(RESULT_CANCELED, returnIntent);

        super.onDestroy();
    }
    */


}
