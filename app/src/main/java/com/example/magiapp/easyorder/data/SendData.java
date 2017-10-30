package com.example.magiapp.easyorder.data;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.magiapp.easyorder.ConfirmOrderActivity;
import com.example.magiapp.easyorder.R;

import java.util.List;

/**
 * Created by MaxMac on 30-Oct-17.
 */

public class SendData extends Thread {
    String ip;
    List<FoodItem> foodItemsList;
    boolean sendStatus = false;
    int tableNum;

    public SendData(String ip, List<FoodItem> foodItemsList, int tableNum){
        this.ip = ip;
        this.foodItemsList = foodItemsList;
        this.tableNum = tableNum;
    }

    @Override
    public void run() {
        /*
        Send a data to server (Por's job)
         */
        try {
            Thread.sleep(2000);
            sendStatus = true;
            Log.d("Thread", "true");

        }catch (InterruptedException e){

        }

        return;
    }

    public boolean isSucess(){
        return sendStatus;
    }
}
