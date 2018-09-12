package com.mobioetech.keralathodoppam.keralathodoppam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AriyipukalDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_heading)
    TextView tvHeading;
    @BindView(R.id.tv_details)
    TextView tvDetails;
    Ariyippukal ariyippukal;
    String details,heading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ariyipukal_detail);
        ButterKnife.bind(this);
        ariyippukal = (Ariyippukal) getIntent().getSerializableExtra("ariyippukal");
        heading = ariyippukal.getHeading();
        details = ariyippukal.getDetails();
        tvDetails.setText(details);
        tvHeading.setText(heading);
    }
}
