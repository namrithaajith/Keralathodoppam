package com.mobioetech.keralathodoppam.keralathodoppam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewCampRequirementsSelectDistrict extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_camp_requirements_select_district);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        ArrayList districts = prepareData();
        DistrictsAdapter adapter = new DistrictsAdapter(getApplicationContext(),districts);
        recyclerView.setAdapter(adapter);



    }
    private ArrayList prepareData() {
        ArrayList locationsArraylist = new ArrayList<>();
        String[] locationsArray = getResources().getStringArray(R.array.districts);

        for(int i=0;i<locationsArray.length;i++)
        {
            locationsArraylist.add(locationsArray[i]);
        }
        return locationsArraylist;

    }
}
