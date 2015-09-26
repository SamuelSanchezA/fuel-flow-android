package com.heavyconnect.heavyconnect;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.heavyconnect.heavyconnect.entities.Equipment;
import com.heavyconnect.heavyconnect.entities.User;
import com.heavyconnect.heavyconnect.rest.EquipmentListResult;
import com.heavyconnect.heavyconnect.resttasks.EquipmentListTask;
import com.heavyconnect.heavyconnect.resttasks.TaskCallback;
import com.heavyconnect.heavyconnect.utils.Constants;
import com.heavyconnect.heavyconnect.utils.StorageUtils;

import java.util.ArrayList;

/**
 * This class allows the user to create a new report.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, ViewTreeObserver.OnGlobalLayoutListener, TaskCallback,
                                                                GoogleMap.OnCameraChangeListener, GoogleMap.OnInfoWindowClickListener {

    public static final String EQUIPS_KEY = "equips";

    private SupportMapFragment mMapFragment;

    private BitmapDescriptor mRedPinIcon;
    private BitmapDescriptor mYellowPinIcon;
    private BitmapDescriptor mGreenPinIcon;

    private FrameLayout mMapContainer;

    private GoogleMap mMap;
    private ArrayList<Marker> mMarkers = new ArrayList<Marker>();

    private ArrayList<Equipment> mEquips = new ArrayList<Equipment>();
    private ProgressDialog mProgress;
    private User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if(!StorageUtils.getIsLoggedIn(this) || (mUser = StorageUtils.getUserData(this)) == null){
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(this, getString(R.string.map_loading), Toast.LENGTH_LONG).show();
            finish();
        }

        MapsInitializer.initialize(this);

        mRedPinIcon = BitmapDescriptorFactory.fromResource(R.drawable.red_pin);
        mYellowPinIcon = BitmapDescriptorFactory.fromResource(R.drawable.yellow_pin);
        mGreenPinIcon = BitmapDescriptorFactory.fromResource(R.drawable.green_pin);

        mMapContainer = (FrameLayout) findViewById(R.id.map_container);

        mMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.map_container, mMapFragment).commit();
        mMapFragment.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle(null);
        mProgress.setMessage(getString(R.string.equip_list_loading));
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mEquips = (ArrayList<Equipment>) extras.getSerializable(EQUIPS_KEY);

            if (mEquips == null)
                mEquips = new ArrayList<Equipment>();
        }else{
            loadEquipments();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method loads the equipment list.
     */
    private void loadEquipments(){
        mEquips.clear();

        if(mProgress != null &&  !mProgress.isShowing())
            mProgress.show();

        new EquipmentListTask(this).execute(mUser);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mMap.setOnCameraChangeListener(this);
        mMap.setOnInfoWindowClickListener(this);

        ViewTreeObserver vto = mMapContainer.getViewTreeObserver();
        if(vto.isAlive()) {
            vto.addOnGlobalLayoutListener(this);
        }

    }

    /**
     * Loads all equipment markers.
     */
    private void showMarkers(){
        Equipment equip;
        BitmapDescriptor icon;
        for(int i = 0; i < mEquips.size(); i++){
            equip = mEquips.get(i);

            if(equip == null)
                continue;

            switch(equip.getStatus()){
                default:
                case Equipment.STATUS_OK:
                    icon = mGreenPinIcon;
                    break;
                case Equipment.STATUS_SERVICE:
                    icon = mYellowPinIcon;
                    break;
                case Equipment.STATUS_BROKEN:
                    icon = mRedPinIcon;
                    break;
            }

            try {
                mMarkers.add(mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(equip.getLatitude(), equip.getLongitude()))
                        .icon(icon)
                        .title(equip.getName())));
            }catch(Exception e){
                finish();
            }
        }

        CameraUpdate cu;
        if(mMarkers.size() == 1) {
            cu = CameraUpdateFactory.newLatLngZoom(mMarkers.get(0).getPosition(), Constants.MAP_DEFAULT_MARKER_SMALL_ZOOM);
            mMap.moveCamera(cu);
        } else if(mMarkers.size() > 1) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : mMarkers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();

            int padding = 0; // offset from edges of the map in pixels
            cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.moveCamera(cu);
        }
    }

    @Override
    public void onCameraChange(CameraPosition position) {
        if (position.zoom > Constants.MAP_DEFAULT_MARKER_SMALL_ZOOM)
            mMap.animateCamera(CameraUpdateFactory.zoomTo(Constants.MAP_DEFAULT_MARKER_SMALL_ZOOM));
    }

    @Override
    public void onGlobalLayout() {
        showMarkers();
        mMapContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onTaskFailed(int errorCode) {
        if(mProgress != null && mProgress.isShowing())
            mProgress.dismiss();

        Toast.makeText(this, getString(R.string.map_load_failure), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onTaskCompleted(Object result) {
        if(mProgress != null && mProgress.isShowing())
            mProgress.dismiss();

        if(!(result instanceof EquipmentListResult)) {
            onTaskFailed(100);
            return;
        }

        mEquips = ((EquipmentListResult) result).getUserEquips();
        showMarkers();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        try {
            Intent intent = new Intent(MapActivity.this, EquipmentRegistrationActivity.class);
            intent.putExtra(EquipmentRegistrationActivity.MODE_KEY, EquipmentRegistrationActivity.EQUIPMENT_DETAILS_MODE);
            intent.putExtra(EquipmentRegistrationActivity.EQUIP_ID_KEY, mEquips.get(mMarkers.indexOf(marker)).getId());
            startActivityForResult(intent, EquipmentRegistrationActivity.EDIT_EQUIPMENT_REQUEST_CODE);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            mMap.clear();
            loadEquipments();
        }
    }
}
