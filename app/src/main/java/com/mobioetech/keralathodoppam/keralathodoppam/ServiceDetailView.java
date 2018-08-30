package com.mobioetech.keralathodoppam.keralathodoppam;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceDetailView extends AppCompatActivity {
    private static final String LOG = "serviceDetailView";
    private FirebaseDatabase database = null;
    private DatabaseReference geofireref,ref_serviceperson;
    private GoogleMap map;
    String phonenumber;

    @BindView(R.id.iv_avatar)
    ImageView mImageView;

    @BindView(R.id.tv_name)
    TextView mtvNameView;

    @BindView(R.id.tv_phone)
    TextView mtvPhoneView;

    @BindView(R.id.tv_areaofservice)
    TextView mEtAreaofservice;

    @BindView(R.id.tv_locality)
    TextView mEtlocality;

    @BindView(R.id.tv_district)
    TextView mtvdistrict;

    @BindView(R.id.bt_submit)
    TextView mBtSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_service_detail_view);
        ButterKnife.bind(this);
        phonenumber = getIntent().getStringExtra("phonenumber");

        database = KeralathodoppamDBUtil.getInstance();
        ref_serviceperson = KeralathodoppamDBUtil.getInstance().getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.SEVANAM).child(SevanamFragment.serviceType).child(phonenumber);
        Log.i(LOG,"ref_serviceperson------->"+ref_serviceperson);
        ref_serviceperson.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Person person = dataSnapshot.getValue(Person.class);
                Log.i(LOG,"person.getName()------>"+person.getName());
                mtvNameView.setText(person.getName());
                mtvPhoneView.setText(person.getPhonenumber());
                mEtAreaofservice.setText(person.getServicetype());
                mEtlocality.setText(person.getLocality());
                mtvdistrict.setText(person.getDistrict());
                mBtSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+person.getPhonenumber()));

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(ServiceDetailView.this,
                                    Manifest.permission.CALL_PHONE)) {
                                new AlertDialog.Builder(getApplicationContext())
                                        .setTitle(getApplicationContext().getResources().getString(R.string.permission))
                                        .setMessage(getApplicationContext().getResources().getString(R.string.permission_callphone))
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //re-request
                                                ActivityCompat.requestPermissions(ServiceDetailView.this,new String[]{Manifest.permission.CALL_PHONE},
                                                        2);
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                            }
                            else{
                                ActivityCompat.requestPermissions(ServiceDetailView.this,
                                        new String[]{Manifest.permission.CALL_PHONE},2);
                            }

                        }
                        else{
                            startActivity(callIntent);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
