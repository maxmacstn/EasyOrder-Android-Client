package com.example.magiapp.easyorder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magiapp.easyorder.data.FoodItem;
import com.example.magiapp.easyorder.data.SendData;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


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
        // getPreference();

    }

    /**
     * Handler for connectServer button
     */
    private class connectServer implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ipVal = ipField.getText().toString();

            if (!validIP(ipVal)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfigActivity.this);
                builder.setMessage("Invalid IPv4 format");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                    }
                });
                builder.show();
                return;
            }


            //Start networking thread.
            final MyAsyncTask task = new MyAsyncTask();
            task.execute(ipVal);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (task.getStatus() == AsyncTask.Status.RUNNING)
                        task.cancel(true);
                }
            }, 5000);

        }
    }

    /**
     * valid IPv4 string ex. "10.0.0.1"
     *
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
     * Do this function after checked that input ip is vaklid
     * @param isSucess
     */
    private void afterCheckedIP(boolean isSucess) {
        if (cb_remember.isChecked()) {
            putPreference();                                 //Save IP
        } else {
            ipPreference.edit().clear().apply();            //Clear saved IP
        }
        connectStatusTV.setText("Connected");
        Intent returnIntent = new Intent();
        String userResponseText = ipVal;
        returnIntent.putExtra("result", userResponseText);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    /**
     * Save ip value
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
     * Get ip value
     * (Obsolete)
     */
    private void getPreference() {
        if (ipPreference.contains("pref_ip"))
            ipField.setText(ipPreference.getString("pref_ip", ""));
        if (ipPreference.contains("pref_save"))
            cb_remember.setChecked(ipPreference.getBoolean("pref_save", false));
    }


    private class MyAsyncTask extends AsyncTask<String, List<FoodItem>, Boolean> {

        private ProgressDialog dialog;
        private boolean isSuccess = false;


        @Override
        protected Boolean doInBackground(String... params) {
            TestConnect testConnect = new TestConnect(params[0]);
            isSuccess = testConnect.send();
            return isSuccess;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ConfigActivity.this);
            dialog = ProgressDialog.show(ConfigActivity.this, "Connecting to server", "Loading");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();
            Log.d("dialog", "Dismiss");
            if (isSuccess) {
                Toast.makeText(ConfigActivity.this, "Connected to server " + ipVal, Toast.LENGTH_LONG).show();
                afterCheckedIP(true);
            } else {
                onCancelled();
            }

        }

        @Override
        protected void onCancelled() {
            AlertDialog.Builder builder = new AlertDialog.Builder(ConfigActivity.this);
            builder.setMessage("Error connecting to server");
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //dialog.dismiss();
                }
            });
            dialog.dismiss();
            builder.show();
        }
    }


    /**
     * Test connect to server
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


}