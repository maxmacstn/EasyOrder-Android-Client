package com.example.magiapp.easyorder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magiapp.easyorder.data.FoodItem;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;



public class WelcomeActivity extends AppCompatActivity {
    private EditText ipField;
    private TextView guideText;
    private Button connect;
    private SharedPreferences ipPreference;
    private CheckBox cb_remember;
    private ScrollView scrollView;
    //private LinearLayout linearLayout;
    //private ImageView bgImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);       //transparent status bar


        ipField = (EditText) findViewById(R.id.et_welcome_ip_field);
        guideText = (TextView) findViewById(R.id.tv_welcome_guideText);
        connect = (Button) findViewById(R.id.b_welcome_Connect);
        scrollView = (ScrollView) findViewById(R.id.welcomeScrollView);
        cb_remember = (CheckBox) findViewById(R.id.cb_rememberIP_welcome);
        //bgImage = (ImageView) findViewById(R.id.im_welcome_bg);
        connect.setOnClickListener(new OnConnectButtonClicked());
        ipPreference = getPreferences(MODE_PRIVATE);
        getPreference();
        ipField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    connectTo();
                }

                return false;
            }
        });

    }


    private class OnConnectButtonClicked implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            connectTo();

        }

    }

    /**
     * Try to connect to server to see weather the ip is valid or not
     *
     * @return true if can connect
     */
    private boolean connectTo() {
        String ipInput = ipField.getText().toString();

        if (!validIP(ipInput)) {
            guideText.setText("Invalid IPv4 format");
            guideText.setTextColor(Color.RED);
            return false;
        } else {
            guideText.setTextColor(Color.BLUE);
            guideText.setText("Connecting..");

            //Start networking thread.
            final MyAsyncTask task = new MyAsyncTask();
            task.execute(ipInput);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (task.getStatus() == AsyncTask.Status.RUNNING)
                        task.cancel(true);
                }
            }, 5000);
            return true;
        }
    }

    /**
     * valid IPv4 string ex. "10.0.0.1"
     * @param ip String of ip
     * @return return true if valid.
     */
    private static boolean validIP(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            if (ip.endsWith(".")) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * do the following function after checked and ip is valid.
     * Check that user want to remember ip or not
     * start MainActivity and put ip as Intent.
     *
     * @param isSuccess is validation process is success or not
     */
    private void afterCheckedIP(boolean isSuccess) {

        if (cb_remember.isChecked()) {
            putPreference();                                 //Save IP
        } else {
            ipPreference.edit().clear().apply();            //Clear saved IP
        }

        /*
        Intent returnIntent = new Intent();
        String userResponseText = ipVal;
        returnIntent.putExtra("result", userResponseText);
        setResult(RESULT_OK, returnIntent);
        */
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        intent.putExtra("ipVal", ipField.getText().toString());
        startActivity(intent);
        finish();
    }


    /**
     * Connect to server thread class.
     *
     */
    private class MyAsyncTask extends AsyncTask<String, List<FoodItem>, Boolean> {

        private ProgressDialog dialog;
        private boolean isSuccess = false;
        private String ipVal;


        @Override
        protected Boolean doInBackground(String... params) {
            WelcomeActivity.TestConnect testConnect = new WelcomeActivity.TestConnect(params[0]);
            ipVal = params[0];
            isSuccess = testConnect.send();
            return isSuccess;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(WelcomeActivity.this);
            dialog = ProgressDialog.show(WelcomeActivity.this, "Connecting to server", "Loading");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();

            if (isSuccess) {
                Toast.makeText(WelcomeActivity.this, "Connected to server " + ipVal, Toast.LENGTH_LONG).show();
                afterCheckedIP(true);
            } else {
                onCancelled();
            }


        }

        @Override
        protected void onCancelled() {

            //For debug purpose, can enter activity with wrong IP address.
            /*
            dialog.dismiss();
            afterCheckedIP(true);
            return;
            */


            AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
            builder.setMessage("Error connecting to server");
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //dialog.dismiss();
                }
            });
            dialog.dismiss();
            builder.show();
            guideText.setText("Connection failed");
            guideText.setTextColor(Color.RED);

        }

    }

    /**
     * Test connect to server.
     */
    private class TestConnect {
        private String ip;
        private boolean sendStatus = false;


        public TestConnect(String ip) {
            this.ip = ip;
        }

        public boolean send() {
            try {
                //Connect
                String url = "http://" + ip + ":8080/order/test";

                //Check response code from server
                if (!checkConnection(url, 5000)) {
                    Log.d("SendData", "Error");
                    return false;
                }

                Thread.sleep(500);          //Load too fast, user not see it's loading.
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;

        }

        public boolean isSuccess() {
            return sendStatus;
        }


        public boolean checkConnection(String url, int timeout) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
                connection.setRequestMethod("HEAD");
                int responseCode = connection.getResponseCode();
                return (200 == responseCode);
            } catch (IOException exception) {
                return false;
            }
        }

    }

    /**
     * save ip value
     */
    private void putPreference() {
        String ip = ipField.getText().toString();
        Boolean isChecked = cb_remember.isChecked();

        SharedPreferences.Editor editor = ipPreference.edit();
        editor.putString("pref_ip", ip);
        editor.putBoolean("pref_save", isChecked);
        editor.apply();
    }

    /**
     * get saved ip value.
     */
    private void getPreference() {
        if (ipPreference.contains("pref_ip"))
            ipField.setText(ipPreference.getString("pref_ip", ""));
        if (ipPreference.contains("pref_save"))
            cb_remember.setChecked(ipPreference.getBoolean("pref_save", false));
    }



}
