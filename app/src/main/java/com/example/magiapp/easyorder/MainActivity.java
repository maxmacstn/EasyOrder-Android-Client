package com.example.magiapp.easyorder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magiapp.easyorder.data.FoodItem;
import com.example.magiapp.easyorder.data.FoodItemTableDataAdapter;
import com.example.magiapp.easyorder.data.MenuMaker;

import java.util.ArrayList;
import java.util.List;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class MainActivity extends AppCompatActivity {


    TextView connectStatus;
    TableView table;
    FoodItemTableDataAdapter foodItemTableDataAdapter;
    Button submit;
    List menuList;
    boolean isDialogShow = false;

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
                ArrayList<FoodItem> orderList = getOrderedList(menuList);
                if (orderList.size() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Must order at least 1 item");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.dismiss();
                        }
                    });
                    builder.show();
                    return;
                }


                Intent intent = new Intent(MainActivity.this, ConfirmOrderActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("menuList",orderList);
                startActivity(intent);
            }
        });
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
        foodItemTableDataAdapter = new FoodItemTableDataAdapter(this,menuList,table);
        table.setHeaderAdapter(new SimpleTableHeaderAdapter(this, foodItemTableDataAdapter.getHeaderData()));
        table.setDataAdapter(foodItemTableDataAdapter);
        table.addDataClickListener(new ClickedTableRow());
        table.addDataLongClickListener(new LongClickedTableRow());

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || resultCode == RESULT_OK) {
            String responseText = data.getStringExtra("result");
            connectStatus.setText("Connected to server at " + responseText);
        }
    }

    /**
     * Display number picker after clicked a table row
     * and update FoodItem object quantity according to the data that previously picked from the DataPicker
     *
     */

    private class ClickedTableRow implements TableDataClickListener{
        @Override
        public void onDataClicked(int rowIndex, Object clickedData) {
            if(isDialogShow)
                return;

            isDialogShow = true;

            final FoodItem rowData = (FoodItem) clickedData;
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
            amountPickerDialog.setCancelable(false);
            amountPickerDialog.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rowData.setQuantity(numberPicker.getValue());                   //Set the quantity in FoodObject from clicked row according to NumberPicker
                    foodItemTableDataAdapter.notifyDataSetChanged();                //Update the table
                    Toast.makeText(getApplicationContext(),rowData.getName()+ " was set quantity to " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                    isDialogShow = false;
                }

            });
            amountPickerDialog.show();

        }
    }

    private class LongClickedTableRow implements TableDataLongClickListener{
        @Override
        public boolean onDataLongClicked(int rowIndex, Object clickedData) {
            Toast.makeText(getApplicationContext(), "Too much order!!",Toast.LENGTH_SHORT).show();
            final FoodItem rowData = (FoodItem) clickedData;
            int currentQty = rowData.getQuantity();



            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            builder.setTitle("Enter quantity for "+ rowData.getName());
            View view = inflater.inflate(R.layout.dialog_edittext, null);
            builder.setView(view);

            final EditText quantityInput = (EditText) view.findViewById(R.id.textinput_dialog);

            builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final int numQuantityInput = Integer.parseInt(quantityInput.getText().toString());
                    if (numQuantityInput <= 50) {
                        rowData.setQuantity(numQuantityInput);                          //Set the quantity in FoodObject from clicked row according to NumberPicker
                        foodItemTableDataAdapter.notifyDataSetChanged();                //Update the table
                        Toast.makeText(getApplicationContext(),rowData.getName()+ " was set quantity to " + numQuantityInput, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Too much order!!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();
            /*

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Enter custom quantity");
            // I'm using fragment here so I'm using getView() to provide ViewGroup
            // but you can provide here any other instance of ViewGroup from your Fragment / Activity
            View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_edittext, null);
            // Set up the input
            final EditText quantityInput = (EditText) viewInflated.findViewById(R.id.textinput_dialog);
            //final int numQuantityInput = Integer.parseInt(quantityInput.getText().toString());

            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            builder.setView(viewInflated);

            // Set up the buttons
            builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            */

            return true;
        }
    }

    /**
     * Get the order from menu list that quantity isn't zero
     * @param menuList List of FoodItem (menuList)
     * @return ArrayList of FoodItem that quantity isn't zero
     */
    private ArrayList<FoodItem> getOrderedList(List<FoodItem> menuList) {
        ArrayList<FoodItem> orderList = new ArrayList<>();

        for (FoodItem item : menuList) {
            if (item.getQuantity() != 0)
                orderList.add(item);
        }
        return orderList;
    }


}
