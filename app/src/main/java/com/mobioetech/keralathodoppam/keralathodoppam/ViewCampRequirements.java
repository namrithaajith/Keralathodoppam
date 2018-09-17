package com.mobioetech.keralathodoppam.keralathodoppam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewCampRequirements extends AppCompatActivity {
    private static final String LOG = "ViewCampRequirements";
    String selectedDistrict,selectedDistrict_eng;
    FirebaseRecyclerAdapter adapter;


    @BindView(R.id.recycler_view)
    RecyclerView rcv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.emptyTextView)
    TextView mEmptyListMessage;

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

        if(selectedDistrict.equals(getResources().getString(R.string.ernakulam_mal))){
            selectedDistrict_eng = getResources().getString(R.string.ernakulam_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.thrissur_mal))){
            selectedDistrict_eng = getResources().getString(R.string.thrissur_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.pathanamthitta_mal))){
            selectedDistrict_eng = getResources().getString(R.string.pathanamthitta_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.alappuzha_mal))){
            selectedDistrict_eng = getResources().getString(R.string.alappuzha_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.kollam_mal))){
            selectedDistrict_eng = getResources().getString(R.string.kollam_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.kottayam_mal))){
            selectedDistrict_eng = getResources().getString(R.string.kottayam_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.kannur_mal))){
            selectedDistrict_eng = getResources().getString(R.string.kannur_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.wayanad_mal))){
            selectedDistrict_eng = getResources().getString(R.string.wayanad_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.palakkad_mal))){
            selectedDistrict_eng = getResources().getString(R.string.palakkad_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.idukki_mal))){
            selectedDistrict_eng = getResources().getString(R.string.idukki_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.alappuzha_mal))){
            selectedDistrict_eng = getResources().getString(R.string.alappuzha_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.thiruvananthapuram_mal))){
            selectedDistrict_eng = getResources().getString(R.string.thiruvananthapuram_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.kasaragod_mal))){
            selectedDistrict_eng = getResources().getString(R.string.kasaragod_eng);
        }
        else if(selectedDistrict.equals(getResources().getString(R.string.kozhikode_mal))){
            selectedDistrict_eng = getResources().getString(R.string.kozhikode_eng);
        }

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
        ref_camprequirements = database.getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.CAMPS).child(selectedDistrict_eng);
        FirebaseRecyclerOptions<CampNeeds> options =
                new FirebaseRecyclerOptions.Builder<CampNeeds>()
                        .setQuery(ref_camprequirements,CampNeeds.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<CampNeeds, FirebaseCampNeedsViewHolder>(options) {


            @Override
            public FirebaseCampNeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_view_camp_requirements_listitem, parent, false);
                return new FirebaseCampNeedsViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            protected void onBindViewHolder(@NonNull FirebaseCampNeedsViewHolder holder, int position, @NonNull CampNeeds model) {
                try {
                    holder.bindCampNeeds(model);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
        rcv.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(adapter);


    }
}
