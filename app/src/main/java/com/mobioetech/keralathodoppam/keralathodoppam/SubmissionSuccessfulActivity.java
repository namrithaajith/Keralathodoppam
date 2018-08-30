package com.mobioetech.keralathodoppam.keralathodoppam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SubmissionSuccessfulActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission_successful);
    }

    public void goHome(View view) {
        Intent intent = new Intent(SubmissionSuccessfulActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
