package com.mobioetech.keralathodoppam.keralathodoppam;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DistrictsAdapter extends RecyclerView.Adapter<DistrictsAdapter.ViewHolder> {

    private ArrayList<String> districts;
    private Context context;
    private static final String LOG = "locationsAdapter";
    String selecteddistrict;


    public DistrictsAdapter(Context context, ArrayList<String> districts) {
        this.context = context;
        this.districts = districts;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.districts_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.locationName.setText(districts.get(position));
        holder.mView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = null;
                selecteddistrict = districts.get(position);
                intent = new Intent(context,ViewCampRequirements.class);
                intent.putExtra("selecteddistrict",selecteddistrict);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return districts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationName;

        public final View mView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            locationName = (TextView)view.findViewById(R.id.location_name);

        }
    }
}
