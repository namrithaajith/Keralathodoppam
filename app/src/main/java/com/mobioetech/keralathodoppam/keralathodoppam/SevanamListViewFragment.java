package com.mobioetech.keralathodoppam.keralathodoppam;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SevanamListViewFragment extends Fragment implements GeoQueryEventListener{
    private static final String LOG = "sevanamlistview";
    String serviceType;
    private FirebaseDatabase database = null;
    private DatabaseReference geofireref,ref_serviceperson;
    private GeoFire geoFire;
    private GeoQuery geoQuery;
    //public static ArrayList phonenumbers;
    public static ArrayList<Person> person_al;
    ServicePersonsAdapter adapter;

    @BindView(R.id.recycler_view)
    RecyclerView rcv_sevanam;
    public SevanamListViewFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sevanamlistview, container, false);
        //phonenumbers = new ArrayList<>();
        person_al = new ArrayList<Person>();
        person_al.clear();

        ButterKnife.bind(this, view);


        Log.i(LOG,"serviceType----->"+serviceType);

        GeoLocation INITIAL_CENTER = new GeoLocation(SevanamFragment.currentlatitude, SevanamFragment.currentlongitude);

        database = KeralathodoppamDBUtil.getInstance();


        geofireref = database.getReference(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.SEVANAM).child(SevanamFragment.serviceType).child(KeralathodoppamConstants.GEOFIRE);
        Log.i(LOG,"geofireref------>"+geofireref);
        this.geoFire = new GeoFire(geofireref);

        this.geoQuery = this.geoFire.queryAtLocation(INITIAL_CENTER, 30);//The radius has to be decreased

        this.geoQuery.addGeoQueryEventListener((GeoQueryEventListener) this);


        rcv_sevanam.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcv_sevanam.setLayoutManager(layoutManager);
        //Log.i(LOG,"beginning phonenumbers---------"+phonenumbers);

        //adapter = new ServicePersonsAdapter(getActivity(), phonenumbers);
        adapter = new ServicePersonsAdapter(getActivity(), person_al);
        rcv_sevanam.setAdapter(adapter);


        return view;
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {

        //phonenumbers.add(key);
        ref_serviceperson = KeralathodoppamDBUtil.getInstance().getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.SEVANAM).child(SevanamFragment.serviceType).child(key);
        Log.i(LOG,"ref_servicepersonon key entered------->"+ref_serviceperson);
        ref_serviceperson.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Person person = dataSnapshot.getValue(Person.class);
                person_al.add(person);
                Log.i(LOG,"person_al-------->"+person_al);
                ServicePersonsAdapter adapter = new ServicePersonsAdapter(getActivity(), person_al);
                rcv_sevanam.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onKeyExited(String key) {
        //phonenumbers.remove(key);
        Log.i(LOG,"on key exited called------person_al"+person_al);

        Iterator<Person> it = person_al.iterator();
        while (it.hasNext()) {
            Person person = it.next();
            if (person.getPhonenumber().equals(key)) {
                it.remove();
                person_al.remove(it);
            }
        }
        Log.i(LOG,"on key exited called------person_al after removal------"+person_al);
        ServicePersonsAdapter adapter = new ServicePersonsAdapter(getActivity(), person_al);
        rcv_sevanam.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        ref_serviceperson = KeralathodoppamDBUtil.getInstance().getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.SEVANAM).child(SevanamFragment.serviceType).child(key);
//        ref_serviceperson.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Person person = dataSnapshot.getValue(Person.class);
//                Log.i(LOG,"person_al.size before----"+person_al.size());
//                person_al.remove(person);
//                for(int i=0 ;i<person_al.size();i++){
//                    if(person.getPhonenumber()==person_al.get(i).getPhonenumber()){
//                        person_al.remove(person);
//                    }
//                }
//                Log.i(LOG,"person_al.size after----"+person_al.size());
//                //Log.i(LOG,"on key exited called------person_al"+person_al);
//                ServicePersonsAdapter adapter = new ServicePersonsAdapter(getActivity(), person_al);
//                rcv_sevanam.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {

    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {

    }
}
