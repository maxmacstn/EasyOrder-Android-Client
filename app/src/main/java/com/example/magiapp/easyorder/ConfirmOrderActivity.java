package com.example.magiapp.easyorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ConfirmOrderActivity extends AppCompatActivity {
    TableView table;
    private static final String[] TABLE_HEADERS = {"Type", "ID", "Name", "Price", "Qty."};
    FoodItemTableDataAdapter foodItemTableDataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Intent i = getIntent();
        List menuList = (ArrayList<FoodItem>) i.getSerializableExtra("menuList");
        List orderList = getOrderedList(menuList);
        table = (TableView<String>) findViewById(R.id.foodConfirmTable);
        initTable(orderList);

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

    private List getOrderedList(List<FoodItem> menu) {
        ArrayList<FoodItem> orderList = new ArrayList<>();

        for (FoodItem item : menu) {
            if (item.getQuantity() != 0)
                orderList.add(item);
        }
        return orderList;
    }
}
