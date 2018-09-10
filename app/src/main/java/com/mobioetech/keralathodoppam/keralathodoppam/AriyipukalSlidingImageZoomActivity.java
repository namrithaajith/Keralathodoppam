package com.mobioetech.keralathodoppam.keralathodoppam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AriyipukalSlidingImageZoomActivity extends AppCompatActivity {

    private static final String LOG = "zoomactivity";
    String image_url;

    @BindView(R.id.expanded_image)
    ImageView expandedImageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ariyipukal_sliding_image_zoom);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        image_url = getIntent().getStringExtra("imageUrl");
        Picasso.get()
                .load(image_url)
                .into(expandedImageView);

    }
}
