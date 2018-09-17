package com.mobioetech.keralathodoppam.keralathodoppam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CampRequirementZoomActivity extends AppCompatActivity {
    private static final String LOG = "CampRequirementZoom";
    String needs_url,imagePath;
    CampNeeds campNeeds;

    @BindView(R.id.expanded_image)
    ImageView expandedImageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_requirement_zoom);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        campNeeds = (CampNeeds) getIntent().getSerializableExtra("campNeeds");
        needs_url = campNeeds.getNeeds_url();
        imagePath = campNeeds.getImagePath();

        getSupportActionBar().setTitle(campNeeds.getCampname());

        Picasso.get()
                .load(needs_url)
                .into(expandedImageView);


    }


}
