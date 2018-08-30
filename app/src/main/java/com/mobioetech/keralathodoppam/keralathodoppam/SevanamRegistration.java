package com.mobioetech.keralathodoppam.keralathodoppam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SevanamRegistration extends AppCompatActivity {

    String serviceType;
    private FirebaseDatabase database = null;
    private DatabaseReference geofireref,ref_serviceperson;
    private GoogleMap map;
    private GeoFire geoFire;
    private GeoQuery geoQuery;

    @BindView(R.id.iv_avatar)
    ImageView mImageView;

    @BindView(R.id.et_name)
    EditText mEtNameView;

    @BindView(R.id.et_phone)
    EditText mEtPhoneView;

    @BindView(R.id.et_areaofservice)
    EditText mEtAreaofservice;

    @BindView(R.id.et_locality)
    EditText mEtlocality;

    @BindView(R.id.et_district)
    EditText mEtdistrict;

    @BindView(R.id.bt_submit)
    TextView mBtSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sevanam_registration);
        ButterKnife.bind(this);

        addTextWatchers();

        database = KeralathodoppamDBUtil.getInstance();
        serviceType = getIntent().getStringExtra("sevanamType");

        mEtPhoneView.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        mEtlocality.setText(SevanamFragment.currentlocality);
        mEtdistrict.setText(SevanamFragment.currentdistrict);
        mEtAreaofservice.setText(serviceType);

        ref_serviceperson = database.getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.SEVANAM).child(serviceType).child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        geofireref = database.getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.SEVANAM).child(serviceType).child(KeralathodoppamConstants.GEOFIRE);
        geoFire = new GeoFire(geofireref);

        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Person person = new Person();
                person.setLatitude(SevanamFragment.currentlatitude);
                person.setLongitude(SevanamFragment.currentlongitude);
                person.setServicetype(mEtAreaofservice.getText().toString());
                person.setLocality(mEtlocality.getText().toString());
                person.setDistrict(mEtdistrict.getText().toString());
                person.setName(mEtNameView.getText().toString());
                person.setPhonenumber(mEtPhoneView.getText().toString());
                ref_serviceperson.setValue(person);

                geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), new GeoLocation(SevanamFragment.currentlatitude, SevanamFragment.currentlongitude), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }
                    }
                });

                Intent intent = new Intent(SevanamRegistration.this,SubmissionSuccessfulActivity.class);
                startActivity(intent);

            }
        });
    }
    private void addTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mEtNameView.addTextChangedListener(textWatcher);
        mEtPhoneView.addTextChangedListener(textWatcher);
        mEtAreaofservice.addTextChangedListener(textWatcher);
        mEtdistrict.addTextChangedListener(textWatcher);
        mEtlocality.addTextChangedListener(textWatcher);
    }

    private void validateInputs() {
        if(mEtNameView.getText() != null && mEtNameView.getText().toString().trim().length() > 0 &&
                mEtPhoneView.getText() != null && mEtPhoneView.getText().toString().trim().length() > 0 &&
                mEtAreaofservice.getText() != null && mEtAreaofservice.getText().toString().trim().length() > 0 &&
                mEtlocality.getText() != null && mEtlocality.getText().toString().trim().length() > 0 &&
                mEtdistrict.getText() != null && mEtdistrict.getText().toString().trim().length() > 0 ) {

            showSubmitButton(true);

        } else {
            showSubmitButton(false);
        }
    }

    private void showSubmitButton(boolean show) {
        if (show) {
            mBtSubmit.setVisibility(View.VISIBLE);
        } else {
            mBtSubmit.setVisibility(View.GONE);
        }
    }

}
