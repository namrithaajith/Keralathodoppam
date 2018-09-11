package com.mobioetech.keralathodoppam.keralathodoppam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DonateFragment extends Fragment {
    @BindView(R.id.img_donate)
    ImageView img_donate;

    public DonateFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_donate, container, false);

        ButterKnife.bind(this,view);

        img_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://donation.cmdrf.kerala.gov.in/#donation"));
                startActivity(browserIntent);
            }
        });
        return view;
    }


}


