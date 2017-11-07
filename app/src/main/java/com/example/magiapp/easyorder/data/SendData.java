package com.example.magiapp.easyorder.data;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.magiapp.easyorder.ConfirmOrderActivity;
import com.example.magiapp.easyorder.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

/**
 * Created by MaxMac on 30-Oct-17.
 */

public class SendData{
    String ip;
    List<FoodItem> foodItemsList;
    boolean sendStatus = false;
    int tableNum;

    public SendData(String ip, List<FoodItem> foodItemsList, int tableNum){
        this.ip = ip;
        this.foodItemsList = foodItemsList;
        this.tableNum = tableNum;
    }

    public boolean send() {

        /*
        @todo: Send a data to server (Por's job)
        Implement your code here
        Maximum time = 6000ms otherwise this function would be killed.
         */

        try {
            //Connect
            String data = "{ \"tableNum\": 78}";
            //String url = "http://localhost:8080/order";

            URL url_server = new URL("http://localhost:8080/order");
            HttpURLConnection httpConn = (HttpURLConnection) url_server.openConnection();
            //HttpURLConnection httpConn = (HttpURLConnection) ((new URL (url).openConnection()));

            httpConn.setDoOutput(true);
            httpConn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpConn.setRequestProperty("Accept", "application/json");
            httpConn.setRequestMethod("POST");

            //httpConn.setDoInput(true);
            httpConn.connect();

            //Write
            OutputStream os = httpConn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data);
            writer.close();
            os.close();

            Log.d("Send data", "Send to "+ip +"\t Table no." + tableNum);
            Log.d("Send data", foodItemsList.toString());
            Thread.sleep(2000);
            sendStatus = true;
            Log.d("Thread", "true");
            return true;

        } catch (InterruptedException e){
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isSuccess(){
        return sendStatus;
    }
}
