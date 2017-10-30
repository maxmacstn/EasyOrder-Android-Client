package com.example.magiapp.easyorder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.magiapp.easyorder.data.FoodItem;
import com.example.magiapp.easyorder.data.FoodItemTableDataAdapter;
import com.example.magiapp.easyorder.data.SendData;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ConfirmOrderActivity extends AppCompatActivity {
    TableView table;
    private static final String[] TABLE_HEADERS = {"Type", "ID", "Name", "Price", "Qty."};
    FoodItemTableDataAdapter foodItemTableDataAdapter;
    List<FoodItem> orderList;
    TextView totalItem;
    TextView totalPrice;
    Button confirmOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Intent i = getIntent();
        orderList = (ArrayList<FoodItem>) i.getSerializableExtra("menuList");
        table = (TableView<String>) findViewById(R.id.foodConfirmTable);
        totalItem = (TextView) findViewById(R.id.confirm_order_totalItem);
        totalPrice = (TextView) findViewById(R.id.confirm_order_totalPrice);
        confirmOrder = (Button) findViewById(R.id.b_confirm_order);
        confirmOrder.setOnClickListener(new OnConfirmOrderClicked());
        initTable(orderList);
        initNumberData();
    }

    private void initTable(List orderList) {
        TableColumnWeightModel columnModel = new TableColumnWeightModel(5);
        columnModel.setColumnWeight(0, 2);
        columnModel.setColumnWeight(1, 1);
        columnModel.setColumnWeight(2, 4);
        columnModel.setColumnWeight(3, 2);
        columnModel.setColumnWeight(4, 2);
        table.setColumnModel(columnModel);
        table.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        foodItemTableDataAdapter = new FoodItemTableDataAdapter(this, orderList, table);
        table.setDataAdapter(foodItemTableDataAdapter);
    }

    private void initNumberData(){
        double price = 0;
        int items = 0;
        for (FoodItem item: orderList) {
            price += item.getQuantity() * item.getPrice();
            items += item.getQuantity();
        }
        totalItem.setText(items + "");
        totalPrice.setText(String.format("%.2f฿",price));
    }

    private class OnConfirmOrderClicked implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            ProgressDialog dialog = new ProgressDialog(ConfirmOrderActivity.this);
            dialog.show(ConfirmOrderActivity.this,"Sending","Loading",false);
            SendData sendData = new SendData("192.168.1.15",orderList,0);
          //  sendData.start();
/*
            final ProgressDialog progress=ProgressDialog.show(ConfirmOrderActivity.this,"Sending","Loading",false);
            new Thread()
            {
                public void run()
                {
                    try{
                        ConfirmOrderActivity.this.runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                
                                // your code
                                try {
                                    Thread.sleep(2000);
                                }catch (InterruptedException e){

                                }
                            }
                        });
                    }
                    catch(Exception e)
                    {
                    }
                    progress.dismiss();
                }
            }.start();
            */
            /*

            while (!sendData.isSucess()){
                //Log.d("test", "loop");
            }*/
            try {
                Thread.sleep(5000);

            }catch (InterruptedException e){

            }

            Log.d("test", "loop");
            dialog.hide();

            //dialog.hide();
        }
    }

}