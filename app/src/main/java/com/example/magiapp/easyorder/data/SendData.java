package com.example.magiapp.easyorder.data;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.magiapp.easyorder.ConfirmOrderActivity;
import com.example.magiapp.easyorder.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Send order data to server
 * This class must run in separated thread.
 * Created by MaxMac on 30-Oct-17.
 */
public class SendData {
    private String ip;
    private List<FoodItem> foodItemsList;
    private boolean sendStatus = false;
    private int tableNum;
    private int uniqueIDfromServer;

    /**
     * @param ip            ip of the server.
     * @param foodItemsList List of food order List.
     * @param tableNum      order table number.
     */
    public SendData(String ip, List<FoodItem> foodItemsList, int tableNum) {
        this.ip = ip;
        this.foodItemsList = foodItemsList;
        this.tableNum = tableNum;
    }

    /**
     * Send data to the server
     * @return return true if success.
     */
    public boolean send() {

        try {
            //Connect
            String url = "http://" + ip + ":8080";

            //Check response code from server
            if (!checkConnection(url + "/order/test", 5000)) {
                Log.d("SendData", "Error: TimedOut");
                return false;
            }

            //Load time is too fast, user not seen it's sending. So I slow it down a little :D
            Thread.sleep(500);



            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            JSONObject json = new JSONObject();


            Log.d("SendData", "Send to " + ip + "\t Table no." + tableNum);
            Log.d("SendData", foodItemsList.toString());

            try {
                HttpPost post = new HttpPost(url + "/order");

                StringEntity se = new StringEntity(getJSONdata().toString(), "UTF-8");
                Log.d("json", getJSONdata().toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

                post.setEntity(se);
                response = client.execute(post);

                    /*Checking response */
                if (response != null) {
                    HttpEntity in = response.getEntity(); //Get the data in the entity
                    String stringResponse = EntityUtils.toString(in);
                    Log.d("SendData", "Server was respond : " + stringResponse);
                    try {
                        uniqueIDfromServer = Integer.parseInt(stringResponse);
                    } catch (NumberFormatException e) {
                        uniqueIDfromServer = -1;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("SendData", "Error");
            }


            sendStatus = true;
            return true;



        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isSuccess() {
        return sendStatus;
    }

    public int getOrderIDfromServer() {
        return uniqueIDfromServer;
    }

    /**
     * Check the connection from server.
     *
     * @param url     url of the server.
     * @param timeout connection timedout.
     * @return return false if response code is not valid or cannot connect to server.
     */
    private boolean checkConnection(String url, int timeout) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            Log.d("CheckConnection", "Server response code: " + responseCode);
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }

    /**
     * get JSONdata from foodItemList
     * @return JSON Object of the foodItemList.
     */
    private JSONObject getJSONdata() {
        JSONObject data = new JSONObject();
        JSONArray foodOrder = new JSONArray();
        try {

            //put all foodOrder into foods
            for (FoodItem item : foodItemsList) {
                JSONObject foodItem = new JSONObject();
                foodItem.put("price", item.getPrice());
                foodItem.put("name", item.getName());
                foodItem.put("quantity", item.getQuantity());
                foodItem.put("id", item.getID());
                foodItem.put("foodType", item.getEnumType());
                //  foodItem.put("available", item.isAvailable());
                foodOrder.put(foodItem);
            }
            //data.put("foods", foodOrder);
            data.put("foodItems", foodOrder);

            data.put("id", 0);
            data.put("tableNum", tableNum);


        } catch (JSONException e) {
            Log.d("getJSONdata", "get json error");
        }

        return data;
    }
}
