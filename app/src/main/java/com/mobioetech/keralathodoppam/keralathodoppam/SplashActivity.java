package com.mobioetech.keralathodoppam.keralathodoppam;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;

public class SplashActivity extends AppCompatActivity {

    private static final String LOG = "splashactivity";
    private FirebaseDatabase database = null;
    private DatabaseReference ref_registered,ref_verified;
    private FirebaseAuth auth;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    ConstraintLayout mConstraintlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int SPLASH_TIME_OUT = 3000;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        database = KeralathodoppamDBUtil.getInstance();
        auth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchActivity();
                finish();
            }
        }, SPLASH_TIME_OUT);


    }

    private void launchActivity() {

        if(auth.getCurrentUser() != null){
            Log.i(LOG,"auth.getCurrentUser(------->"+auth.getCurrentUser().getPhoneNumber());
            Intent intent = new Intent(SplashActivity.this ,MainActivity.class);
            startActivity(intent);

        }
        else{
            Log.i(LOG,"owner not logged in");
            Intent intent = new Intent(SplashActivity.this ,LoginActivity.class);
            startActivity(intent);
        }

    }
}
