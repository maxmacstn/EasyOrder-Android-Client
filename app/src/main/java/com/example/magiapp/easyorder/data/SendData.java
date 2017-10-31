package com.example.magiapp.easyorder.data;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.magiapp.easyorder.ConfirmOrderActivity;
import com.example.magiapp.easyorder.R;

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
        Send a data to server (Por's job)

        Implement your code here

        Maximum time = 6000ms otherwise this function would be killed.
         */
        try {
            Log.d("Send data", "Send to "+ip +"\t Table no." + tableNum);
            Log.d("Send data", foodItemsList.toString());
            Thread.sleep(2000);
            sendStatus = true;
            Log.d("Thread", "true");
            return true;

        }catch (InterruptedException e){

        }

        return false;
    }

    public boolean isSuccess(){
        return sendStatus;
    }
}
