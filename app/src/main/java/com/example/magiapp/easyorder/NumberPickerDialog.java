package com.example.magiapp.easyorder;

/**
 * Created by MaxMac on 26-Oct-17.
 */


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NumberPickerDialog extends Activity {

    private Button upButton, downButton, ok;
    private TextView editText;
    private int uprange = 60;
    private int downrange = 5;
    private int values = 5;
// AlertDialog.Builder builder;
// AlertDialog mDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_picker);


        editText = (TextView) findViewById(R.id.numberEditText);

        upButton = (Button) findViewById(R.id.upButton);
        upButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
               // downButton.setBackgroundResource(R.drawable.timepicker_down_normal);
                //upButton.setBackgroundResource(R.drawable.timepicker_up_pressed);
                if (values >= downrange && values <= uprange)
                    values += 5;
                if (values > uprange)
                    values = downrange;
                editText.setText("" + values);

            }
        });

        downButton = (Button) findViewById(R.id.downButton);
        downButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //downButton.setBackgroundResource(R.drawable.timepicker_down_pressed);
                //upButton.setBackgroundResource(R.drawable.timepicker_up_normal);
                if (values >= downrange && values <= uprange)
                    values -= 5;

                if (values < downrange)
                    values = uprange;

                editText.setText(values + "");
            }
        });

        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NumberPickerDialog.this.finish();
            }
        });

    }
}


