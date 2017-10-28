package com.example.magiapp.easyorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.magiapp.easyorder.data.FoodItem;
import com.example.magiapp.easyorder.data.FoodItemTableDataAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Intent i = getIntent();
        orderList = (ArrayList<FoodItem>) i.getSerializableExtra("menuList");
        table = (TableView<String>) findViewById(R.id.foodConfirmTable);
        totalItem = (TextView) findViewById(R.id.confirm_order_totalItem);
        totalPrice = (TextView) findViewById(R.id.confirm_order_totalPrice);
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

}