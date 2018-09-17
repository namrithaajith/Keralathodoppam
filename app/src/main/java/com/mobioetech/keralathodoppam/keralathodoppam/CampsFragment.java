package com.mobioetech.keralathodoppam.keralathodoppam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CampsFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView rcv_camps;

    public CampsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camps, container, false);

        ButterKnife.bind(this,view);

        rcv_camps.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcv_camps.setLayoutManager(layoutManager);


        ArrayList sevanamTypes = prepareData();
        CampsAdapter adapter = new CampsAdapter(getActivity(),sevanamTypes);
        rcv_camps.setAdapter(adapter);

        return view;
    }

    private ArrayList prepareData() {
        ArrayList requirement_type = new ArrayList<>();
        String[] typesArray = getResources().getStringArray(R.array.camprequirementtype);
        String[] urlsArray = getResources().getStringArray(R.array.camprequirementtype_imageUrls);
        for(int i=0;i<typesArray.length;i++)
        {
            SevanamTypes sevanamTypes = new SevanamTypes();
            sevanamTypes.setSevanam_type(typesArray[i]);
            sevanamTypes.setSevanam_type_url(urlsArray[i]);

            requirement_type.add(sevanamTypes);
        }
        return requirement_type;
    }

}


