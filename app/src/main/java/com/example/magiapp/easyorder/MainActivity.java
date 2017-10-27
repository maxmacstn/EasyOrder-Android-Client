package com.example.magiapp.easyorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ObbInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;


public class MainActivity extends AppCompatActivity {


    private static final String[][] DATA_TO_SHOW = {{"001", "Rice", "20฿"}, {"002", "Apple Juice", "50฿"}, {"003", "Rice", "20฿"}, {"004", "Apple Juice", "50฿"},
            {"005", "Rice", "20฿"}, {"006", "Apple Juice", "50฿"}, {"007", "Rice", "20฿"}, {"008", "Apple Juice", "50฿"},
            {"009", "Rice", "20฿"}, {"010", "Apple Juice", "50฿"}, {"011", "Rice", "20฿"}, {"012", "Apple Juice", "50฿"},
            {"013", "Rice", "20฿"}, {"014", "Apple Juice", "50฿"}, {"015", "Rice", "20฿"}, {"016", "Apple Juice", "50฿"}};
    private static final String[] TABLE_HEADERS = {"Type","ID", "Name", "Price","Qty."};
    TextView connectStatus;
    TableView table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        table = (TableView<String>) findViewById(R.id.foodTable);

        connectStatus = (TextView) findViewById(R.id.tv_connectStatus_main);
        setSupportActionBar(toolbar);
        initTable();




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
        columnModel.setColumnWeight(0, 1);
        columnModel.setColumnWeight(1, 1);
        columnModel.setColumnWeight(2, 3);
        columnModel.setColumnWeight(3, 2);
        columnModel.setColumnWeight(4, 2);
        table.setColumnModel(columnModel);
        table.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        final FoodItemTableDataAdapter foodItemTableDataAdapter = new FoodItemTableDataAdapter(this,MenuMaker.createFoodMenuList(),table);
        table.setDataAdapter(foodItemTableDataAdapter);


        table.addDataClickListener(new TableDataClickListener() {
                                       @Override
                                       public void onDataClicked(int rowIndex, Object clickedData) {
                                           final FoodItem rowData = (FoodItem) clickedData;
                                           Toast.makeText(MainActivity.this, "U clicked" + rowData.getName(), Toast.LENGTH_SHORT).show();

                                             final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(MainActivity.this)
                                                   .minValue(0)
                                                   .maxValue(10)
                                                   .defaultValue(0)
                                                   .backgroundColor(Color.WHITE)
                                                   .separatorColor(Color.TRANSPARENT)
                                                   .textColor(Color.BLACK)
                                                   .textSize(20)
                                                   .enableFocusability(false)
                                                   .wrapSelectorWheel(true)
                                                   .build();

                                           AlertDialog.Builder amountPickerDialog = new AlertDialog.Builder(MainActivity.this);
                                           amountPickerDialog.setTitle("Select Amount");
                                           amountPickerDialog.setView(numberPicker);
                                           amountPickerDialog.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                                                       @Override
                                                       public void onClick(DialogInterface dialog, int which) {
                                                           Toast.makeText(getApplicationContext(),"You Picked " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                                                           rowData.setQuantity(numberPicker.getValue());
                                                           table.invalidate();
                                                           //Snackbar.make(findViewById(R.id.), "You picked : " + numberPicker.getValue(), Snackbar.LENGTH_LONG).show();
                                                       }
                                                   })
                                                   .show();

                                            /*
                                           AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                           builder.setMessage("รับขนมจีบซาลาเปาเพิ่มมั้ยครับ?");
                                           builder.setPositiveButton("รับ", new DialogInterface.OnClickListener() {
                                               public void onClick(DialogInterface dialog, int id) {
                                                   Toast.makeText(getApplicationContext(),
                                                           "ขอบคุณครับ", Toast.LENGTH_SHORT).show();
                                               }
                                           });
                                           builder.setNegativeButton("ไม่รับ", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   //dialog.dismiss();
                                               }
                                           });
                                           builder.show();



                                           NumberDialog myDiag =
                                                   NumberDialog.newInstance(3, 123);
                                           myDiag.show(getFragmentManager(), "Diag");

                                           */
/*
                                           Intent i;
                                           i = new Intent(MainActivity.this, NumberPickerDialog.class);
                                           startActivity(i);
*/
                                       }
                                   }
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || resultCode == RESULT_OK) {
            String responseText = data.getStringExtra("result");
            connectStatus.setText("Connected to server at " + responseText);
        }
    }

    class ProductClick implements TableDataClickListener<String[]> {
        @Override
        public void onDataClicked(int rowIndex, String[] clickedData) {
            String clickedCarString = clickedData[0] + clickedData[1];
            Toast.makeText(MainActivity.this, clickedCarString, Toast.LENGTH_SHORT).show();
        }
    }

}
