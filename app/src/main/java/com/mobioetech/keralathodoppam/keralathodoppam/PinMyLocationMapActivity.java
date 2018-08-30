package com.mobioetech.keralathodoppam.keralathodoppam;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PinMyLocationMapActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMarkerDragListener {
    private static final String LOG = "pinmylocation";
    private static final int INITIAL_ZOOM_LEVEL = 14;
    public static double pinnedlatitude;
    public static double pinnedlongitude;
    public static String pinnedaddress;
    private GoogleMap map;

    @BindView(R.id.tv_address)
    TextView tvaddress;

    Geocoder geocoder;
    List<Address> addresses = null;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_my_location);
        ButterKnife.bind(this);

        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(SevanamFragment.currentlatitude, SevanamFragment.currentlongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String locality = addresses.get(0).getLocality();
//        String district = addresses.get(0).getSubAdminArea();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName();
        tvaddress.setText(address);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
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

        map.setOnMarkerDragListener(this);
        //MarkerOptions.draggable(boolean);

        LatLng latlng = new LatLng(SevanamFragment.currentlatitude,SevanamFragment.currentlongitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(SevanamFragment.currentlatitude, SevanamFragment.currentlongitude), INITIAL_ZOOM_LEVEL));
        map.addMarker(new MarkerOptions().position(latlng)
                .title(getResources().getString(R.string.markerdrag))
        .draggable(true));

        map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.i(LOG,"onMarkerDragEnd------->"+marker.getPosition().latitude+","+marker.getPosition().longitude);
        pinnedlatitude = marker.getPosition().latitude;
        pinnedlongitude = marker.getPosition().longitude;

        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(pinnedlatitude, pinnedlongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String locality = addresses.get(0).getLocality();
//        String district = addresses.get(0).getSubAdminArea();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName();
        tvaddress.setText(address);
        pinnedaddress = address;

    }
}
