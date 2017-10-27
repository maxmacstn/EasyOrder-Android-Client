package com.example.magiapp.easyorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigActivity extends AppCompatActivity {
    Button connectButton;
    EditText ipField;
    TextView connectStatusTV;
    String ipVal;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        connectButton = (Button) findViewById(R.id.b_connect);
        ipField = (EditText) findViewById(R.id.et_ipField);
        connectStatusTV = (TextView) findViewById(R.id.tv_connectStatus);
        connectButton.setOnClickListener(new connectServer());
    }

    public class connectServer implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            ipVal = ipField.getText().toString();
            Toast.makeText(ConfigActivity.this, "Connected to server "+ ipVal , Toast.LENGTH_SHORT ).show();
            connectStatusTV.setText("Connected");

            Intent returnIntent = new Intent();
            String userResponseText = ipVal;
            returnIntent.putExtra("result", userResponseText);
            setResult(RESULT_OK, returnIntent);
            finish();

        }
    }

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


}
