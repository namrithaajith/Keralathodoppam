package com.mobioetech.keralathodoppam.keralathodoppam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AskForSevanam extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView rcv_sevanam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_sevanam);
        ButterKnife.bind(this);

        rcv_sevanam.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcv_sevanam.setLayoutManager(layoutManager);


        ArrayList sevanamTypes = prepareData();
        AskForSevanamAdapter adapter = new AskForSevanamAdapter(this, sevanamTypes);
        rcv_sevanam.setAdapter(adapter);

    }

    private ArrayList prepareData() {
        ArrayList sevanam_type = new ArrayList<>();
        String[] typesArray = getResources().getStringArray(R.array.services);
        String[] urlsArray = getResources().getStringArray(R.array.services_url);
        for (int i = 0; i < typesArray.length; i++) {
            SevanamTypes sevanamTypes = new SevanamTypes();
            sevanamTypes.setSevanam_type(typesArray[i]);
            sevanamTypes.setSevanam_type_url(urlsArray[i]);

            sevanam_type.add(sevanamTypes);
        }
        return sevanam_type;
    }
}
