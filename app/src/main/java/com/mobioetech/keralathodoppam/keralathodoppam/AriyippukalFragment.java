package com.mobioetech.keralathodoppam.keralathodoppam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AriyippukalFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView rcv_ariyippukal;

    public AriyippukalFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ariyippukal, container, false);

        ButterKnife.bind(this,view);
        return view;
    }
}


