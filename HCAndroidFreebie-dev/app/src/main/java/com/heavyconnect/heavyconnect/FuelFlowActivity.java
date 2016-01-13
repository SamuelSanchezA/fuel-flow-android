package com.heavyconnect.heavyconnect;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.heavyconnect.heavyconnect.database.EquipmentDAO;
import com.heavyconnect.heavyconnect.database.SQLiteHelper;
import com.heavyconnect.heavyconnect.entities.Equipment;

import java.nio.DoubleBuffer;
import java.util.Random;
import java.util.UUID;

/**
 * Created by jsanchez-garcia on 10/24/15.
 * This class represents the Fuel Consumption screen
 */
public class FuelFlowActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BluetoothFlowSensor";
    public static final String FUEL_FLOW_RATE = "fuel flow rate";
    public static final String TOTAL_FUEL_FLOW = "total fuel flow";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_ENABLE_BT = 3;

    private BluetoothAdapter btAdapter = null;

    // Well known SPP UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Insert your bluetooth devices MAC address
    private static String address = "00:00:00:00:00:00";

  private   LinearLayout mBlue;
    private Button bluetoothimage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuel_flow_bluetooth);

        btAdapter = BluetoothAdapter.getDefaultAdapter();


        bluetoothimage = (Button) findViewById(R.id.blueButton);
        bluetoothimage.setOnClickListener(this);

        checkBTState();

        insertValueIntoDatabase();


    }
    //

    public void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on

        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth Not supported. Aborting.");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth is enabled...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

            }
        }
    }
    public void errorExit(String title, String message){
        Toast msg = Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_SHORT);
        msg.show();
        finish();
    }


    @Override
    public void onClick(View v) {

        startActivity(new Intent(this, DeviceListActivity.class));
    }

    /**
     * This current function will be used for testing values into the database
     */
    public void insertValueIntoDatabase()
    {
        double temp_fuel_rate = 20;
        double temp_total_fuel_consumption = 0;
        int ID = 1324;



        ContentValues temp = new ContentValues(3);
        temp.clear();
        EquipmentDAO object = new EquipmentDAO(this);
        object.open();
        object.removeAll();

        for(int i = 0; i < 10.; i++) {

            temp_fuel_rate += 2;
            temp_total_fuel_consumption++;

            temp.put(SQLiteHelper.EQUIPS_FUEL_FLOW_RATE, temp_fuel_rate);
            temp.put(SQLiteHelper.EQUIPS_FUEL_FLOW_TOTAL_CONSUMPTION, temp_total_fuel_consumption);
            temp.put(SQLiteHelper.EQUIPS_COLUMN_ID, ID);

            object.put_fuel_flow(temp);

        }
            show_values(object);

    }

    public void show_values(EquipmentDAO object)
    {
        ContentValues temp = object.getMinFuelFlowRate(1324);
        Double avg = object.getAvgFuelFlowRate(1324);

        if(temp == null) {
            errorExit("Error", "error on line 125 in fuelFlowActivy class");
            return;
        }

        else {
            int id = temp.getAsInteger(SQLiteHelper.EQUIPS_COLUMN_ID);
            double fuel_flow = temp.getAsDouble(SQLiteHelper.EQUIPS_FUEL_FLOW_RATE);
            double total_consumption = temp.getAsDouble(SQLiteHelper.EQUIPS_FUEL_FLOW_TOTAL_CONSUMPTION);
            String date_time = temp.getAsString(SQLiteHelper.EQUIPS_DATETIME);

            EditText tractor_name, fuel_flow_rate, DateTime;

            tractor_name = (EditText) findViewById(R.id.tractor_name_val);
            tractor_name.setText(Integer.toString(id) + " and avg: " + avg);

            fuel_flow_rate = (EditText) findViewById(R.id.tractor_asset_val);
            fuel_flow_rate.setText(fuel_flow + " and " + total_consumption);

            DateTime = (EditText) findViewById(R.id.Engine_Equipment_used_vals);
            DateTime.setText(date_time);

        }


    }
}

