package com.mobioetech.keralathodoppam.keralathodoppam;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AskForSevanamDetail extends AppCompatActivity {
    private static final String LOG = "askforsevanamdetail";
    String serviceType;

    private FirebaseDatabase database = null;
    private DatabaseReference ref_requiresevanam;

    Geocoder geocoder;
    List<Address> addresses = null;

    @BindView(R.id.et_name)
    EditText mEtNameView;

    @BindView(R.id.et_phone)
    EditText mEtPhoneView;

    @BindView(R.id.et_service)
    EditText mEtservice;

    @BindView(R.id.et_address)
    EditText mEtaddress;

    @BindView(R.id.bt_submit)
    TextView mBtSubmit;

    @BindView(R.id.imvmap)
    ImageView imgmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_sevanam_detail);
        ButterKnife.bind(this);
        database = KeralathodoppamDBUtil.getInstance();
        serviceType = getIntent().getStringExtra("sevanamType");

        if(PinMyLocationMapActivity.pinnedaddress == null){
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(SevanamFragment.currentlatitude, SevanamFragment.currentlongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            mEtaddress.setText(address);
        }
        else{
            mEtaddress.setText(PinMyLocationMapActivity.pinnedaddress);
        }

        mEtPhoneView.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        mEtservice.setText(serviceType);
        addTextWatchers();
        imgmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AskForSevanamDetail.this,PinMyLocationMapActivity.class);
                startActivity(intent);
            }
        });


        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long tsLong = System.currentTimeMillis()/1000;
                final String ts = tsLong.toString();
                ref_requiresevanam = database.getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.SEVANAMREQUIREMENTS).child(serviceType).child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                ServiceRequest serviceRequest = new ServiceRequest();
                serviceRequest.setCurrentlatitude(SevanamFragment.currentlatitude);
                serviceRequest.setCurrentlongitude(SevanamFragment.currentlongitude);
                serviceRequest.setCurrentaddress(mEtaddress.getText().toString());
                serviceRequest.setName(mEtNameView.getText().toString());
                serviceRequest.setPhonenumber(mEtPhoneView.getText().toString());
                serviceRequest.setPinnedaddress(PinMyLocationMapActivity.pinnedaddress);
                serviceRequest.setPinnedlatitude(PinMyLocationMapActivity.pinnedlatitude);
                serviceRequest.setPinnedlongitude(PinMyLocationMapActivity.pinnedlongitude);
                serviceRequest.setTimestamp(ts);
                ref_requiresevanam.setValue(serviceRequest);

                Intent intent = new Intent(AskForSevanamDetail.this,SubmissionSuccessfulActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(PinMyLocationMapActivity.pinnedaddress != null){
            mEtaddress.setText(PinMyLocationMapActivity.pinnedaddress);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    }

    private void validateInputs() {
        if(//mEtNameView.getText() != null && mEtNameView.getText().toString().trim().length() > 0 &&
                mEtPhoneView.getText() != null && mEtPhoneView.getText().toString().trim().length() > 0)

        {

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
