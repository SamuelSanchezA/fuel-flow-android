package com.heavyconnect.heavyconnect;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.heavyconnect.heavyconnect.entities.User;
import com.heavyconnect.heavyconnect.utils.StorageUtils;

/**
 * This class represents the Grid screen.
 */
public class GridActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mEquips;
    private LinearLayout mMap;
    private LinearLayout mSettings;
    private LinearLayout mExit;

    private LinearLayout mUnlock;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        if(!StorageUtils.getIsLoggedIn(this) || (mUser = StorageUtils.getUserData(this)) == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, getString(R.string.grid_user_isnt_logged_in), Toast.LENGTH_LONG).show();
            finish();
        }

        mEquips = (LinearLayout) findViewById(R.id.grid_equip);
        mEquips.setOnClickListener(this);

        mMap = (LinearLayout) findViewById(R.id.grid_map);
        mMap.setOnClickListener(this);

        mSettings = (LinearLayout) findViewById(R.id.grid_settings);
        mSettings.setOnClickListener(this);

        mExit = (LinearLayout) findViewById(R.id.grid_exit);
        mExit.setOnClickListener(this);

        mUnlock = (LinearLayout) findViewById(R.id.grid_lock);
        mUnlock.setOnClickListener(this);

        if(mUser.isEmployee()){
            mSettings.setVisibility(View.GONE);
        }else{
            mSettings.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.grid_equip: // Equipments
                startActivity(new Intent(this, EquipmentListActivity.class));
                break;
            case R.id.grid_map:
                startActivity(new Intent(this, MapActivity.class));
                break;
            case R.id.grid_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.grid_exit: // Logout
                logout();
                break;
            case R.id.grid_lock:
                startActivity(new Intent(this,UnlockActivity.class));
                break;
        }
    }

    /**
     * This method logs out the user.
     */
    private void logout(){
        StorageUtils.clearPrefs(this);
        StorageUtils.putIsLoggedIn(this, false);
        Toast.makeText(this, getString(R.string.logout_message), Toast.LENGTH_LONG).show();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
