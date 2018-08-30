package com.mobioetech.keralathodoppam.keralathodoppam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProvideCampRequirementscopy30aug extends AppCompatActivity {

    String serviceType;
    private final int PICK_IMAGE_REQUEST = 71;
    private FirebaseDatabase database = null;
    FirebaseStorage storage;
    StorageReference storageReference;
    private DatabaseReference geofireref,ref_campneeds;
    private GoogleMap map;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    private static final String LOG = "providecamprequirements";
    private Uri filePath;

    @BindView(R.id.et_name)
    EditText mEtNameView;

    @BindView(R.id.et_phone)
    EditText mEtPhoneView;

    @BindView(R.id.et_needs)
    EditText mEtneeds;

    @BindView(R.id.et_locality)
    EditText mEtlocality;

    @BindView(R.id.et_district)
    EditText mEtdistrict;

    @BindView(R.id.bt_submit)
    TextView mBtSubmit;

    @BindView(R.id.bt_selectpic)
    TextView btnselectpic;

    @BindView(R.id.imgView)
    ImageView imgselected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_camp_requirements);
        ButterKnife.bind(this);
        addTextWatchers();

        database = KeralathodoppamDBUtil.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mEtPhoneView.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        mEtlocality.setText(SevanamFragment.currentlocality);
        mEtdistrict.setText(SevanamFragment.currentdistrict);

        btnselectpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

                ref_campneeds = database.getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.CAMPS).child(mEtdistrict.getText().toString().toUpperCase()).push();

                Long tsLong = System.currentTimeMillis()/1000;
                final String ts = tsLong.toString();

                if(filePath != null)
                {
                    final ProgressDialog progressDialog = new ProgressDialog(ProvideCampRequirementscopy30aug.this);
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();
                    Task<Uri> urlTask = ref.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            // Continue with the task to get the download URL
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                CampNeeds campNeeds = new CampNeeds();
                                campNeeds.setNeeds_url(downloadUri.toString());
                                campNeeds.setLatitudeofpersonregistered(SevanamFragment.currentlatitude);
                                campNeeds.setLongitudeofpersonregister(SevanamFragment.currentlongitude);
                                campNeeds.setNeeds(mEtneeds.getText().toString());
                                campNeeds.setLocality(mEtlocality.getText().toString());
                                campNeeds.setDistrict(mEtdistrict.getText().toString());
                                campNeeds.setCampname(mEtNameView.getText().toString());
                                campNeeds.setPhonenumber(mEtPhoneView.getText().toString());
                                campNeeds.setTimestamp(ts);
                                ref_campneeds.setValue(campNeeds);

                                Intent intent = new Intent(ProvideCampRequirementscopy30aug.this,SubmissionSuccessfulActivity.class);
                                startActivity(intent);

                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ProvideCampRequirementscopy30aug.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });



                }
                else{
                    CampNeeds campNeeds = new CampNeeds();
                    campNeeds.setLatitudeofpersonregistered(SevanamFragment.currentlatitude);
                    campNeeds.setLongitudeofpersonregister(SevanamFragment.currentlongitude);
                    campNeeds.setNeeds(mEtneeds.getText().toString());
                    campNeeds.setLocality(mEtlocality.getText().toString());
                    campNeeds.setDistrict(mEtdistrict.getText().toString());
                    campNeeds.setCampname(mEtNameView.getText().toString());
                    campNeeds.setPhonenumber(mEtPhoneView.getText().toString());
                    campNeeds.setTimestamp(ts);
                    ref_campneeds.setValue(campNeeds);

                    Intent intent = new Intent(ProvideCampRequirementscopy30aug.this,SubmissionSuccessfulActivity.class);
                    startActivity(intent);
                }




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
        mEtneeds.addTextChangedListener(textWatcher);
        mEtdistrict.addTextChangedListener(textWatcher);
        mEtlocality.addTextChangedListener(textWatcher);
    }

    private void validateInputs() {
        if(mEtNameView.getText() != null && mEtNameView.getText().toString().trim().length() > 0 &&
                mEtPhoneView.getText() != null && mEtPhoneView.getText().toString().trim().length() > 0 &&
                //mEtneeds.getText() != null && mEtneeds.getText().toString().trim().length() > 0 &&
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgselected.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
