package com.mobioetech.keralathodoppam.keralathodoppam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AriyippukalFragment extends Fragment {

    private static final String LOG = "ariyipukal";

    @BindView(R.id.recycler_view)
    RecyclerView rcv;

    private DatabaseReference ref_slidingimages,ref_information;

    private int currentPage = 0;

    private ArrayList<String> ImagesArray = new ArrayList<String>();

    PagerAdapter mPagerAdapter;

    private FirebaseRecyclerAdapter adapter;

    @BindView(R.id.pager)
    ViewPager mPager;

    @BindView(R.id.indicator)
    CirclePageIndicator indicator;


    public AriyippukalFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ariyippukal, container, false);

        ButterKnife.bind(this,view);

        setUpInformationRecyclerView();
        initViewPager();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(LOG,"adapter--------------->"+adapter);
        Log.i(LOG,"onStart called ariyipukal fragment");
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(LOG,"onStop called ariyipukal fragment");
        adapter.stopListening();
    }

    private void setUpInformationRecyclerView() {
        ref_information = KeralathodoppamDBUtil.getInstance().getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.ARIYIPPUKAL).child(KeralathodoppamConstants.INFORMATION);
        Log.i(LOG,"ref_information---adapter--->"+ref_information);

        FirebaseRecyclerOptions<Ariyippukal> options =
                new FirebaseRecyclerOptions.Builder<Ariyippukal>()
                        .setQuery(ref_information,Ariyippukal.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Ariyippukal, FirebaseAriyippukalViewHolder>(options) {


            @NonNull
            @Override
            public FirebaseAriyippukalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_view_ariyippukal_listitem, parent, false);
                Log.i(LOG,"onCreateViewHolder---FirebaseAriyippukalViewHolder---->");
                return new FirebaseAriyippukalViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull FirebaseAriyippukalViewHolder holder, int position, @NonNull Ariyippukal model) {
                holder.bindAriyippukal(model);
                Log.i(LOG,"onBindViewHolder---FirebaseAriyippukalViewHolder---->");
            }
        };

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcv.setLayoutManager(layoutManager);
        rcv.setAdapter(adapter);

    }

    private void initViewPager() {

        ref_slidingimages = KeralathodoppamDBUtil.getInstance().getReference(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.ARIYIPPUKAL).child(KeralathodoppamConstants.SLIDINGIMAGES);
        Log.i(LOG,"ref_slidingimages----->"+ ref_slidingimages);
        ref_slidingimages.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String imageurl = snapshot.getValue(AriyipukalSlidingImages.class).getImage_url();
                    ImagesArray.add(imageurl);
                }

                mPagerAdapter = new SlidingImage_Adapter(getContext(),ImagesArray);

                mPager.setAdapter(mPagerAdapter);

                indicator.setViewPager(mPager);

                final float density = getResources().getDisplayMetrics().density;

                indicator.setRadius(5 * density);

                indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        currentPage = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
