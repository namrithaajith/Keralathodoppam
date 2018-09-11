package com.mobioetech.keralathodoppam.keralathodoppam;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.key;

public class SevanamMapViewFragment extends Fragment implements GeoQueryEventListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener{
    private static final String LOG = "sevanamMapview";
    private static final int INITIAL_ZOOM_LEVEL = 14;
    private FirebaseDatabase database = null;
    private DatabaseReference geofireref, ref_service,ref_identifyphonecall;
    private GoogleMap map;
    private GeoFire geoFire;
    private GeoQuery geoQuery;


    private Map<String, Marker> markers;


    @BindView(R.id.rootFrame)
    FrameLayout rootFrame;

    @BindView(R.id.rootll)
    LinearLayout rootll;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    ArgbEvaluator argbEvaluator;



    public SevanamMapViewFragment() {
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sevanammapview, container, false);

        Log.i(LOG,"serviceType------>"+SevanamFragment.serviceType);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        Log.i(LOG,"mapFragment------>"+mapFragment);
        ButterKnife.bind(this, view);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        argbEvaluator = new ArgbEvaluator();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int devHeight = displayMetrics.heightPixels;
        int devWidth = displayMetrics.widthPixels;

        setUpPagerAdapter();
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(-devWidth / 2);



        return view;
    }
    private void setUpPagerAdapter() {

        //List<Integer> data = Arrays.asList(0, 1);
        List<Integer> data = Arrays.asList(0);
        NearbyDetailInfoPagerAdapter adapter = new NearbyDetailInfoPagerAdapter(data);
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(LOG,"inside on map ready");
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        map.setOnMarkerClickListener(this);


        if(SevanamFragment.mLastLocation != null)
        {
            Log.i(LOG,"SevanamFragment.mLastLocation------>"+SevanamFragment.mLastLocation);
            GeoLocation INITIAL_CENTER = new GeoLocation(SevanamFragment.currentlatitude, SevanamFragment.currentlongitude);

            database = KeralathodoppamDBUtil.getInstance();

            geofireref = database.getReference(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.SEVANAM).child(SevanamFragment.serviceType).child(KeralathodoppamConstants.GEOFIRE);
            Log.i(LOG,"geofireref------>"+geofireref);
            this.geoFire = new GeoFire(geofireref);

            this.geoQuery = this.geoFire.queryAtLocation(INITIAL_CENTER, 30);//The radius has to be decreased

            this.geoQuery.addGeoQueryEventListener(this);

            this.markers = new HashMap<String, Marker>();

            //this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentlatitude, currentlongitude), INITIAL_ZOOM_LEVEL));
            /*this.map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                    .target(new LatLng(currentlatitude, currentlongitude))
                    .zoom(INITIAL_ZOOM_LEVEL)
                    .bearing(0)
                    .tilt(45)
                    .build()), 10000, null);*/
        }

    }
    @Override
    public void onStart() {
        if(geoQuery != null){
            this.geoQuery.addGeoQueryEventListener(this);
        }
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        if(geoQuery != null) {
            this.geoQuery.removeAllListeners();
        }
        for (Marker marker: this.markers.values()) {
            this.markers.remove(key);
            marker.remove();


        }
        this.markers.clear();

    }

    @Override
    public void onKeyEntered(final String key, final GeoLocation location) {

        final Location autoLoc = new Location("autoLocation");
        autoLoc.setLatitude(location.latitude);
        autoLoc.setLongitude(location.longitude);
        LatLng latLngCenter = new LatLng(autoLoc.getLatitude(), autoLoc.getLongitude());

        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, INITIAL_ZOOM_LEVEL));
        /*this.map.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder()
                .target(latLngCenter)
                .zoom(INITIAL_ZOOM_LEVEL)
                .bearing(0)
                .tilt(45)
                .build()), 10000, null);*/

        //final Marker marker = this.map.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.icondrive )));
        final Marker marker = this.map.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)));

        marker.setTag(key);

        this.markers.put(key, marker);


    }

    @Override
    public void onKeyExited(String key) {

        Marker marker = this.markers.get(key);
        if (marker != null) {
            marker.remove();
            this.markers.remove(key);
        }

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        Marker marker = this.markers.get(key);

        Log.i(LOG,"On key moved called......");

        if (marker != null) {
            this.animateMarkerTo(marker, location.latitude, location.longitude);

        }

    }
    @Override
    public void onGeoQueryError(DatabaseError error) {
        new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onGeoQueryReady() {

    }


    private void animateMarkerTo(final Marker marker, final double lat, final double lng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long DURATION_MS = 3000;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final LatLng startPosition = marker.getPosition();
        handler.post(new Runnable() {
            @Override
            public void run() {
                float elapsed = SystemClock.uptimeMillis() - start;
                float t = elapsed/DURATION_MS;
                float v = interpolator.getInterpolation(t);

                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                marker.setPosition(new LatLng(currentLat, currentLng));

                // if animation is not finished yet, repeat
                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {

        final String markerkey = (String) marker.getTag();
        final String[] name = new String[1];
        final String[] service_type = new String[1];
        final String[] locality = new String[1];
        final String[] district = new String[1];
        final String[] phone = new String[1];

        ref_service = database.getReference(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.SEVANAM).child(SevanamFragment.serviceType).child(markerkey);

        ref_service.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Person person = dataSnapshot.getValue(Person.class);
                if(person != null){
                    name[0] = person.getName();
                    service_type[0] = person.getServicetype();
                    locality[0] = person.getLocality();
                    district[0] = person.getDistrict();
                    phone[0] = person.getPhonenumber();
                    viewPager.setVisibility(View.VISIBLE);

                    ImageView imageView = (ImageView) viewPager.findViewById(R.id.personimage);
                    TextView tv_personname = (TextView)viewPager.findViewById(R.id.personname);
                    TextView tv_personphone = (TextView)viewPager.findViewById(R.id.personphone);
                    TextView tv_servicetype = (TextView)viewPager.findViewById(R.id.servicetype);
                    TextView tv_place = (TextView)viewPager.findViewById(R.id.place);
                    Button btn_call = (Button)viewPager.findViewById(R.id.btn_call);
                    tv_personname.setText(name[0]);
                    tv_personphone.setText(phone[0]);
                    tv_servicetype.setText(service_type[0]);
                    Log.i(LOG,"service_type[0]------->"+service_type[0]);
                    tv_place.setText(locality[0]+","+district[0]);
                    btn_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Long tsLong = System.currentTimeMillis()/1000;
                            String ts = tsLong.toString();

                            ref_identifyphonecall = KeralathodoppamDBUtil.getInstance().getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.IDENTIFYPHONECALLS).child(KeralathodoppamConstants.SEVANAM).push();
                            IdentifyPhoneCall phonecall = new IdentifyPhoneCall();
                            phonecall.setCallfrom(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                            phonecall.setCallto(phone[0]);
                            phonecall.setTimestamp(ts);
                            ref_identifyphonecall.setValue(phonecall);

                            if(phone[0] != null)
                            {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:"+phone[0]));
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                            Manifest.permission.CALL_PHONE)) {
                                        new AlertDialog.Builder(getContext())
                                                .setTitle(getActivity().getResources().getString(R.string.permission))
                                                .setMessage(getActivity().getResources().getString(R.string.permission_callphone))
                                                .setCancelable(false)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //re-request
                                                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},
                                                                2);
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();

                                    }
                                    else{
                                        ActivityCompat.requestPermissions(getActivity(),
                                                new String[]{Manifest.permission.CALL_PHONE},2);
                                    }

                                }
                                else{
                                    getActivity().startActivity(callIntent);
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


       return false;

    }


//    @Override
//    public void onBackPressed() {
//
//        if (viewPager.getVisibility() == View.VISIBLE) {
//
//            TransitionManager.beginDelayedTransition(rootFrame);
//            viewPager.setVisibility(View.INVISIBLE);
//            map.setPadding(0, 0, 0, 0);
//            return;
//        }
//
//        startActivity(new Intent(this, MainActivityUser.class));
//        finish();
//    }


}


