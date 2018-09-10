package com.mobioetech.keralathodoppam.keralathodoppam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewCampRequirements extends AppCompatActivity {
    private static final String LOG = "ViewCampRequirements";
    String selectedDistrict;
    FirebaseRecyclerAdapter adapter;


    @BindView(R.id.recycler_view)
    RecyclerView rcv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FirebaseDatabase database = null;

    private DatabaseReference ref_camprequirements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_camp_requirements);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.campneeds));

        selectedDistrict = getIntent().getStringExtra("selecteddistrict");
        database = KeralathodoppamDBUtil.getInstance();

        setupFirebaseadapter();

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void setupFirebaseadapter(){
        ref_camprequirements = database.getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.CAMPS).child(selectedDistrict);
        Log.i(LOG,"ref_camprequirements------>"+ref_camprequirements);
        FirebaseRecyclerOptions<CampNeeds> options =
                new FirebaseRecyclerOptions.Builder<CampNeeds>()
                        .setQuery(ref_camprequirements,CampNeeds.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<CampNeeds, FirebaseCampNeedsViewHolder>(options) {


            @Override
            public FirebaseCampNeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_view_camp_requirements_listitem, parent, false);
                Log.i(LOG,"onCreateViewHolder------->");
                return new FirebaseCampNeedsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FirebaseCampNeedsViewHolder holder, int position, @NonNull CampNeeds model) {
                holder.bindCampNeeds(model);
                Log.i(LOG,"onBindViewHolder------->");
            }
        };
        rcv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(adapter);


    }
}
