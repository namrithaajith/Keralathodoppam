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

public class ViewWhoRequireSevanam extends AppCompatActivity {

    private static final String LOG = "viewwhorequiresevanam";
    FirebaseRecyclerAdapter adapter;

    @BindView(R.id.recycler_view)
    RecyclerView rcv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private FirebaseDatabase database = null;


    private DatabaseReference ref_whorequiresevanam;
    String sevanam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_who_require_sevanam);
        ButterKnife.bind(this);

        switch(SevanamFragment.serviceType) {
            case "Cleaning":
                sevanam = getResources().getString(R.string.cleaning);
                break;
            case "Electrical":
                sevanam = getResources().getString(R.string.electrical);
                break;
            case "Plumbing":
                sevanam = getResources().getString(R.string.plumbing);
                break;
            case "Mechanical":
                sevanam = getResources().getString(R.string.mechanical);
                break;
            case "Civil":
                sevanam = getResources().getString(R.string.civil);
                break;
            case "Painting":
                sevanam = getResources().getString(R.string.painting);
                break;
            case "Health":
                sevanam = getResources().getString(R.string.health);
                break;
            case "Insurance":
                sevanam = getResources().getString(R.string.insurance);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(sevanam);

        database = KeralathodoppamDBUtil.getInstance();
        ref_whorequiresevanam = database.getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.SEVANAMREQUIREMENTS).child(SevanamFragment.serviceType);
        Log.i(LOG,"ref_whorequiresevanam------>"+ref_whorequiresevanam);
        FirebaseRecyclerOptions<ServiceRequest> options =
                new FirebaseRecyclerOptions.Builder<ServiceRequest>()
                        .setQuery(ref_whorequiresevanam,ServiceRequest.class)
                        .build();
        Log.i(LOG,"options------>"+options);

        adapter = new FirebaseRecyclerAdapter<ServiceRequest, FirebaseServiceRequestViewHolder>(options) {


            @Override
            public FirebaseServiceRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_view_who_require_sevanam_listitem, parent, false);
                Log.i(LOG,"inside FirebaseServiceRequestViewHolder onCreateViewHolder");

                return new FirebaseServiceRequestViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FirebaseServiceRequestViewHolder holder, int position, @NonNull ServiceRequest model) {
                Log.i(LOG,"inside onBindViewHolder onBindViewHolder");
                holder.bindRequest(model);
            }
        };

        rcv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(adapter);

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


}
