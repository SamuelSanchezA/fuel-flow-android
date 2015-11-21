package com.heavyconnect.heavyconnect;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by jsanchez-garcia on 10/24/15.
 * This class represents the Fuel Consumption screen
 */
public class FuelFlowActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "BluetoothFlowSensor";

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



    }


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


}

