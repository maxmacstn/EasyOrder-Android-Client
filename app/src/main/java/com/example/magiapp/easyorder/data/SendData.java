package com.example.magiapp.easyorder.data;

import java.util.List;

/**
 * Created by MaxMac on 30-Oct-17.
 */

public class SendData implements Runnable {
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
            Thread.sleep(5000);

        }catch (InterruptedException e){

        }


    }

    public boolean isSucess(){
        return sendStatus;
    }
}
