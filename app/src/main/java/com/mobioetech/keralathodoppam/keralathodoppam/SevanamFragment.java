package com.mobioetech.keralathodoppam.keralathodoppam;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SevanamFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private static final String LOG = "sevanamfragment";
    private static final int REQUEST_LOCATION = 0;
    public static Location mLastLocation;
    public static double currentlatitude;
    public static double currentlongitude;
    public static String currentdistrict;
    public static String currentlocality;
    public static String serviceType;


    @BindView(R.id.recycler_view)
    RecyclerView rcv_sevanam;

    public SevanamFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildGoogleApiClient();


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sevanam, container, false);

        ButterKnife.bind(this, view);

        rcv_sevanam.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcv_sevanam.setLayoutManager(layoutManager);


        ArrayList sevanamTypes = prepareData();
        SevanamAdapter adapter = new SevanamAdapter(getActivity(), sevanamTypes);
        rcv_sevanam.setAdapter(adapter);

        return view;
    }

    private ArrayList prepareData() {
        ArrayList sevanam_type = new ArrayList<>();
        String[] typesArray = getResources().getStringArray(R.array.sevanam_types);
        String[] urlsArray = getResources().getStringArray(R.array.sevanam_types_imageUrls);
        for (int i = 0; i < typesArray.length; i++) {
            SevanamTypes sevanamTypes = new SevanamTypes();
            sevanamTypes.setSevanam_type(typesArray[i]);
            sevanamTypes.setSevanam_type_url(urlsArray[i]);

            sevanam_type.add(sevanamTypes);
        }
        return sevanam_type;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.permission))
                        .setMessage(getString(R.string.permissions_not_granted_location_access))
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //re-request
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_LOCATION);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }


        } else {
            getLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATION) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();

            } else {
                requestAlert(getString(R.string.permissions_not_granted_location_access));
            }
        }
    }

    private void requestAlert(String msg) {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.permission))
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_LOCATION);

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLastLocation != null) {
            currentlatitude = mLastLocation.getLatitude();
            currentlongitude = mLastLocation.getLongitude();

            Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(currentlatitude, currentlongitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                currentdistrict = addresses.get(0).getSubAdminArea();
                currentlocality = addresses.get(0).getLocality();
            }

            }

    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG," google API Client Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG," google API Client Connection failed");
    }
}
