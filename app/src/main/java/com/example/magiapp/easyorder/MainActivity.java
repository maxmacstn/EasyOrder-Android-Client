package com.example.magiapp.easyorder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class MainActivity extends AppCompatActivity {


    private static final String[] TABLE_HEADERS = {"Type","ID", "Name", "Price","Qty."};
    TextView connectStatus;
    TableView table;
    FoodItemTableDataAdapter foodItemTableDataAdapter;
    Button submit;
    List menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        table = (TableView<String>) findViewById(R.id.foodTable);
        submit = (Button) findViewById(R.id.b_submit);
        connectStatus = (TextView) findViewById(R.id.tv_connectStatus_main);
        menuList = MenuMaker.createFoodMenuList();
        setSupportActionBar(toolbar);
        initTable();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConfirmOrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("menuList",((ArrayList)menuList));
                startActivity(intent);
            }
        });


/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "U just press settings button", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
            //intent.putExtra("result", ans);
            startActivityForResult(intent, 1);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initTable() {
        TableColumnWeightModel columnModel = new TableColumnWeightModel(5);
        columnModel.setColumnWeight(0, 2);
        columnModel.setColumnWeight(1, 1);
        columnModel.setColumnWeight(2, 4);
        columnModel.setColumnWeight(3, 2);
        columnModel.setColumnWeight(4, 2);
        table.setColumnModel(columnModel);
        table.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        foodItemTableDataAdapter = new FoodItemTableDataAdapter(this,menuList,table);
        table.setDataAdapter(foodItemTableDataAdapter);
        table.addDataClickListener(new ClickedTableRow());

    }

    /**
     * Display number picker after clicked a table row
     * and update FoodItem object quantity according to the data that previously picked from the DataPicker
     *
     */

    private class ClickedTableRow implements TableDataClickListener{
        @Override
        public void onDataClicked(int rowIndex, Object clickedData) {
            final FoodItem rowData = (FoodItem) clickedData;
            //Toast.makeText(MainActivity.this, "U clicked" + rowData.getName(), Toast.LENGTH_SHORT).show();
            int currentQty = rowData.getQuantity();

            final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(MainActivity.this)
                    .minValue(0)
                    .maxValue(10)
                    .defaultValue(currentQty)
                    .backgroundColor(Color.WHITE)
                    .separatorColor(Color.TRANSPARENT)
                    .textColor(Color.BLACK)
                    .textSize(20)
                    .enableFocusability(false)
                    .wrapSelectorWheel(true)
                    .build();

            AlertDialog.Builder amountPickerDialog = new AlertDialog.Builder(MainActivity.this);
            amountPickerDialog.setTitle("Select "+ rowData.getName() + " quantity");
            amountPickerDialog.setView(numberPicker);
            amountPickerDialog.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rowData.setQuantity(numberPicker.getValue());                   //Set the quantity in FoodObject from clicked row according to NumberPicker
                    foodItemTableDataAdapter.notifyDataSetChanged();                //Update the table
                    Toast.makeText(getApplicationContext(),rowData.getName()+ " was set quantity to " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();

                }

            });
            amountPickerDialog.show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || resultCode == RESULT_OK) {
            String responseText = data.getStringExtra("result");
            connectStatus.setText("Connected to server at " + responseText);
        }
    }


}
