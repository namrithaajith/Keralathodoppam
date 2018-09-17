package com.mobioetech.keralathodoppam.keralathodoppam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final String LOG = "mainactivity";
    private static final String UNCHANGED_CONFIG_VALUE = "CHANGE-ME";
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth auth;
    private ActionBarDrawerToggle mDrawerToggle;
    private View headerview;
    private TextView loginbtn;
    private ImageView avatarImageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        if(!isLocationServiceEnabled()){
            Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(myIntent);
        }

        if(mNavigationView!=null)
        {

            setupDrawerContent(mNavigationView);

            headerview = mNavigationView.getHeaderView(0);
            loginbtn = (TextView) headerview.findViewById(R.id.login_text);
            auth = FirebaseAuth.getInstance();
            loginbtn.setText(auth.getCurrentUser().getPhoneNumber());

        }


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    public boolean isLocationServiceEnabled(){
        LocationManager locationManager = null;
        boolean gps_enabled= false,network_enabled = false;

        if(locationManager ==null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){
            //do nothing...
        }

        try{
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){
            //do nothing...
        }

        return gps_enabled || network_enabled;

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AriyippukalFragment(), getResources().getString(R.string.ariyippukal));
        adapter.addFragment(new SevanamFragment(), getResources().getString(R.string.sevanam));
        adapter.addFragment(new CampsFragment(), getResources().getString(R.string.camps));
        viewPager.setAdapter(adapter);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                mDrawerLayout.closeDrawer(mNavigationView);
                                break;
                            case R.id.nav_logout:
                                AuthUI.getInstance()
                                        .signOut(MainActivity.this)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Log.i(LOG, getResources().getString(R.string.sign_out_failed));
                                                }
                                            }
                                        });


                                break;

                            case R.id.nav_donate:
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://donation.cmdrf.kerala.gov.in/#donation"));
                                startActivity(browserIntent);
                                break;
                            case R.id.nav_delete:
                                if (auth.getCurrentUser() != null){
                                    AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                                            .setMessage("Are you sure you want to delete this account?")
                                            .setPositiveButton("Yes, nuke it!", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    deleteAccount();
                                                }
                                            })
                                            .setNegativeButton("No", null)
                                            .create();

                                    dialog.show();
                                }


                                break;

                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                System.out.println("Inside on drawer opened");
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                System.out.println("Inside on drawer closed");
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }
    private void deleteAccount() {
        FirebaseAuth.getInstance()
                .getCurrentUser()
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.i(LOG, getResources().getString(R.string.delete_account_failed));
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
            mDrawerLayout.closeDrawer(mNavigationView);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null && item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mNavigationView)) {
                mDrawerLayout.closeDrawer(mNavigationView);

            } else {
                mDrawerLayout.openDrawer(mNavigationView);
            }
            return true;
        }

        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @MainThread
    private boolean isGoogleConfigured() {
        return !UNCHANGED_CONFIG_VALUE.equals(
                getResources().getString(R.string.default_web_client_id));
    }

    @MainThread
    private boolean isFacebookConfigured() {
        return !UNCHANGED_CONFIG_VALUE.equals(
                getResources().getString(R.string.facebook_application_id));
    }



}
