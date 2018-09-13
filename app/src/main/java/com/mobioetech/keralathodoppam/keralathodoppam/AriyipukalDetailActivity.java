package com.mobioetech.keralathodoppam.keralathodoppam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AriyipukalDetailActivity extends AppCompatActivity {
    private static final String LOG = "ariyippukal";
    @BindView(R.id.tv_heading)
    TextView tvHeading;
    @BindView(R.id.tv_details)
    TextView tvDetails;
    @BindView(R.id.support_image)
    ImageView imvSupportImage;
    Ariyippukal ariyippukal;
    String details,heading,supportimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ariyipukal_detail);
        ButterKnife.bind(this);
        ariyippukal = (Ariyippukal) getIntent().getSerializableExtra("ariyippukal");
        heading = ariyippukal.getHeading();
        details = ariyippukal.getDetails();
        supportimage = ariyippukal.getSupport_image();
        tvDetails.setText(details);
        tvHeading.setText(heading);
        Log.i(LOG,"supportimage----->"+supportimage.length());
        if(supportimage != null ){
            Picasso.get()
                    .load(supportimage)
                    .placeholder(R.drawable.prayforkerala1)
                    .into(imvSupportImage);
        }
    }

}
