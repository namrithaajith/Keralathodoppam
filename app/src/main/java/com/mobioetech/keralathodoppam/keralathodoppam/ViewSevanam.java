package com.mobioetech.keralathodoppam.keralathodoppam;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewSevanam extends AppCompatActivity {
    private static final String LOG = "viewsevanam";
    String serviceType,sevanam;
    private FirebaseDatabase database = null;
    private DatabaseReference geofireref,ref_serviceperson;
    private GoogleMap map;
    private GeoFire geoFire;
    private GeoQuery geoQuery;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sevanam);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        tabLayout.setupWithViewPager(viewPager);
        database = KeralathodoppamDBUtil.getInstance();
        serviceType = getIntent().getStringExtra("sevanamType");
        Log.i(LOG,"serviceType------->"+serviceType);
        switch(serviceType){
            case "Cleaning":sevanam = getResources().getString(R.string.cleaning);
                break;
            case "Electrical":sevanam = getResources().getString(R.string.electrical);
                break;
            case "Plumbing":sevanam = getResources().getString(R.string.plumbing);
                break;
            case "Mechanical":sevanam = getResources().getString(R.string.mechanical);
                break;
            case "Civil":sevanam = getResources().getString(R.string.civil);
                break;
            case "Painting":sevanam = getResources().getString(R.string.painting);
                break;
            case "Health":sevanam = getResources().getString(R.string.health);
                break;
            case "Insurance":sevanam = getResources().getString(R.string.insurance);

        }
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SevanamListViewFragment(), sevanam);
        adapter.addFragment(new SevanamMapViewFragment(), getResources().getString(R.string.map));
        viewPager.setAdapter(adapter);
    }

}
