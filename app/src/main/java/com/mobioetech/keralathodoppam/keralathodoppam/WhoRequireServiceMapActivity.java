package com.mobioetech.keralathodoppam.keralathodoppam;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class WhoRequireServiceMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String LOG = "WhoRequireService";
    String phone_number;
    double pinned_latitude,pinned_longitude,current_latitude,current_longitude;
    private static final int INITIAL_ZOOM_LEVEL = 14;
    private GoogleMap map;
    ServiceRequest serviceRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_require_service_map);
        serviceRequest = (ServiceRequest) getIntent().getSerializableExtra("serviceRequest");
        phone_number = serviceRequest.getPhonenumber();
        Log.i(LOG,"phone_number---------"+phone_number);
        pinned_latitude = serviceRequest.getPinnedlatitude();
        pinned_longitude = serviceRequest.getPinnedlongitude();
        current_latitude = serviceRequest.getCurrentlatitude();
        current_longitude = serviceRequest.getCurrentlongitude();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng latlng;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        Log.i(LOG,"pinned_latitude----->"+pinned_latitude);
        if(pinned_latitude == 0.0){
            latlng = new LatLng(current_latitude,current_longitude);
        }else{
            latlng = new LatLng(pinned_latitude,pinned_longitude);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,INITIAL_ZOOM_LEVEL));
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(SevanamFragment.currentlatitude, SevanamFragment.currentlongitude), INITIAL_ZOOM_LEVEL));
        map.addMarker(new MarkerOptions().position(latlng)
                .title(serviceRequest.getName()));


    }
}
