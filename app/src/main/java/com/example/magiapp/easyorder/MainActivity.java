package com.example.magiapp.easyorder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magiapp.easyorder.data.FoodItem;
import com.example.magiapp.easyorder.data.FoodItemComparator;
import com.example.magiapp.easyorder.data.FoodItemTableDataAdapter;
import com.google.gson.Gson;
import com.tooltip.Tooltip;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;



public class MainActivity extends AppCompatActivity {


    private TextView connectStatus;
    private SortableTableView table;
    private FoodItemTableDataAdapter foodItemTableDataAdapter;
    private Button submit;
    private List<FoodItem> menuList;
    boolean isDialogShow = false;
    private EditText actionBarET_tableNum;
    private String ipVal;
    private MenuItem b_selectTableNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getActionBar().setIcon(R.drawable.easy_order_title_banner);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        b_selectTableNum = (MenuItem) findViewById(R.id.action_ButtonSelectTableNum);
        table = (SortableTableView<String>) findViewById(R.id.foodTable);
        submit = (Button) findViewById(R.id.b_submit);
        connectStatus = (TextView) findViewById(R.id.tv_connectStatus_main);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        ipVal = intent.getStringExtra("ipVal");
        connectStatus.setText(ipVal);
        menuList = new ArrayList<>();
        initTable();
        updateTableMenu();


        submit.setOnClickListener(new OnClickedSubmitButton());
        //  autoInitRandomOrder();

    }

    @Override
    /**
     * Init ActionBar to contain OptionMenu.
     * Create TableNum entry and TableNum Button.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem tableNumMenu = menu.findItem(R.id.action_selectTableNum);
        final EditText et_tableNum = (EditText) MenuItemCompat.getActionView(tableNumMenu);
        et_tableNum.setHint("-");
        et_tableNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_tableNum.setMaxWidth(100);
        et_tableNum.setGravity(Gravity.CENTER);
        et_tableNum.setImeOptions(EditorInfo.IME_ACTION_SEND);
        et_tableNum.setHeight(200);
        et_tableNum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});

        this.actionBarET_tableNum = et_tableNum;

        actionBarET_tableNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    actionBarET_tableNum.clearFocus();
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(actionBarET_tableNum.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    //tableNum = Integer.parseInt(actionBarET_tableNum.getText().toString());
                }
                return false;
            }
        });


        Tooltip tooltip = new Tooltip.Builder(actionBarET_tableNum)
                .setText("Enter table number here.")
                .setCancelable(true)
                //   .setBackgroundColor(Color.parseColor("#A0009688"))
                .setBackgroundColor(Color.parseColor("#CCFFFFFF"))
                .setTextColor(Color.parseColor("#00665c"))
                .setCornerRadius(R.dimen.default_tooltip_padding)
                .show();


        return true;
    }

    @Override
    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }

        if (id == R.id.action_resetOrder) {
            resetTable();
            return true;
        }

        if (id == R.id.action_ButtonSelectTableNum) {
            actionBarET_tableNum.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(actionBarET_tableNum, InputMethodManager.SHOW_IMPLICIT);
            return true;
        }
        if (id == R.id.action_refreshList)
            updateTableMenu();


        return super.onOptionsItemSelected(item);
    }

    /**
     * Initialize the Sortable table view with data from list
     */
    private void initTable() {

        //Set the width of the table.
        TableColumnWeightModel columnModel = new TableColumnWeightModel(5);
        columnModel.setColumnWeight(0, 4);
        columnModel.setColumnWeight(1, 3);
        columnModel.setColumnWeight(2, 6);
        columnModel.setColumnWeight(3, 4);
        columnModel.setColumnWeight(4, 3);
        table.setColumnModel(columnModel);

        //Add option to table.
        foodItemTableDataAdapter = new FoodItemTableDataAdapter(this, menuList, table);
        table.setHeaderAdapter(new SimpleTableHeaderAdapter(this, foodItemTableDataAdapter.getHeaderData()));
        table.setDataAdapter(foodItemTableDataAdapter);
        table.addDataClickListener(new ClickedTableRow());
        table.addDataLongClickListener(new LongClickedTableRow());
        table.setColumnComparator(0, FoodItemComparator.typeComparator);
        table.setColumnComparator(1, FoodItemComparator.idComparator);
        table.setColumnComparator(2, FoodItemComparator.nameComparator);

    }


    @Override
    /**
     * Get result from returned activity.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String responseText = data.getStringExtra("result");
            ipVal = responseText;
            connectStatus.setText(responseText);
            updateTableMenu();
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            resetTable();
        }
    }


    /**
     * Display number picker after clicked a table row
     * and update FoodItem object quantity according to the data that previously picked from the DataPicker
     */
    private class ClickedTableRow implements TableDataClickListener {
        @Override
        public void onDataClicked(int rowIndex, Object clickedData) {
            if (isDialogShow)
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
            amountPickerDialog.setTitle("Select " + rowData.getName() + " quantity");
            amountPickerDialog.setView(numberPicker);
            amountPickerDialog.setCancelable(false);
            amountPickerDialog.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    rowData.setQuantity(numberPicker.getValue());                   //Set the quantity in FoodObject from clicked row according to NumberPicker
                    foodItemTableDataAdapter.notifyDataSetChanged();                //Update the table
                    Toast.makeText(getApplicationContext(), rowData.getName() + " was set quantity to " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
                    isDialogShow = false;
                }

            });
            amountPickerDialog.show();

        }
    }

    /**
     * Display number text field after long clicked a table row
     * and update FoodItem object quantity according to the data that previously entered value from the textField
     */
    private class LongClickedTableRow implements TableDataLongClickListener {
        @Override
        public boolean onDataLongClicked(int rowIndex, Object clickedData) {
            final FoodItem rowData = (FoodItem) clickedData;
            int currentQty = rowData.getQuantity();


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            builder.setTitle("Enter quantity for " + rowData.getName());
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
                        Toast.makeText(getApplicationContext(), rowData.getName() + " was set quantity to " + numQuantityInput, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Too much order!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();

            return true;
        }
    }


    /**
     * Action Listener when clicked submit button.
     * - Get order from menuList and send to ConfirmOrderActivity
     */
    private class OnClickedSubmitButton implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ArrayList<FoodItem> orderList = getOrderedList(menuList);
            if (orderList.size() == 0) {
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

            int tableNum = 0;
            if (actionBarET_tableNum.length() != 0) {
                tableNum = Integer.parseInt(actionBarET_tableNum.getText().toString());
            }

            if (tableNum == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Please input table number in the top right field.");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Tooltip tooltip = new Tooltip.Builder(actionBarET_tableNum)
                                .setText("Enter table number here.")
                                .setCancelable(true)
                                //   .setBackgroundColor(Color.parseColor("#A0009688"))
                                .setBackgroundColor(Color.parseColor("#CCFFFFFF"))
                                .setTextColor(Color.parseColor("#00665c"))
                                .setCornerRadius(R.dimen.default_tooltip_padding)
                                .show();
                    }
                });
                builder.show();


                return;
            }


            Intent intent = new Intent(MainActivity.this, ConfirmOrderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("menuList", orderList);
            intent.putExtra("tableNum", tableNum);
            intent.putExtra("ipVal", ipVal);
            startActivityForResult(intent, 2);
        }
    }


    /**
     * Get the order from menu list that quantity isn't zero
     *
     * @param menuList List of FoodItem (menuList)
     * @return ArrayList of FoodItem that quantity isn't zero
     */
    private ArrayList<FoodItem> getOrderedList(List<FoodItem> menuList) {
        ArrayList<FoodItem> orderList = new ArrayList<>();

        for (FoodItem item : menuList) {
            if (item.getQuantity() != 0)
                orderList.add(item);
        }
        Collections.sort(orderList, FoodItemComparator.typeComparator);
        return orderList;
    }


    /**
     * This method will auto random the order for easier debugging process
     * Auto order 5-10 items from all items in list
     * Auto set quantity from 1-5
     */
    private void autoInitRandomOrder() {


        for (int i = 0; i < (int) (Math.random() * 10 + 5); i++) {
            menuList.get((int) (Math.random() * menuList.size())).setQuantity((int) (Math.random() * 5 + 1));                   //Set the quantity in FoodObject from clicked row according to NumberPicker
        }
        foodItemTableDataAdapter.notifyDataSetChanged();

    }

    /**
     * Reset table quantity to 0 for all row.
     * Set action bar table number field to blank.
     */
    private void resetTable() {

        for (int i = 0; i < menuList.size(); i++) {
            menuList.get(i).setQuantity(0);
        }
        foodItemTableDataAdapter.notifyDataSetChanged();
        actionBarET_tableNum.setText("");

    }

    /*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ipVal = savedInstanceState.getString("ipVal");
        Log.d("restore ipVal", ipVal);
    }
    */

    /**
     * Update the table by getting menuList from the server.
     */
    private void updateTableMenu() {
        MenuDownloader menuDownloader = new MenuDownloader();
        menuDownloader.execute(ipVal);
    }

    /**
     * This class download the menuList from the server.
     * url of the server that want to connect.
     */
    private class MenuDownloader extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        private String output;
        private boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Downloading menu data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;


            try {
                URL url = new URL("http://" + params[0] + ":8080/food");
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(2000);
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                //output = json from server that converted to String
                output = buffer.toString();
                isSuccess = true;
                Thread.sleep(500);

                return output;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();


            //if not success, show the dialog and go back to WelcomeActivity
            if (!isSuccess) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Error while getting menu data from server :(");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
                return;
            }

            Log.d("GetJSONMenu", output);


            //Convert String to JSONArray and use Gson Library to menuList List.
            JSONArray array = null;
            try {
                array = new JSONArray(output);
                menuList.clear();

                if (array.length() > 0) {
                    Gson gson = new Gson();
                    int i = 0;
                    while (i < array.length()) {
                        menuList.add(gson.fromJson(array.getJSONObject(i).toString(), FoodItem.class));
                        i++;
                    }
                } else {
                    //if no foodItem object from server
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("There are no food in menu :(");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    builder.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("MenuList", menuList.toString());
            foodItemTableDataAdapter.notifyDataSetChanged();
        }
    }

}
