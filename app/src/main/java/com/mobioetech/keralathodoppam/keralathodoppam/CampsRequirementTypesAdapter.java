package com.mobioetech.keralathodoppam.keralathodoppam;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CampsRequirementTypesAdapter extends RecyclerView.Adapter<CampsRequirementTypesAdapter.ViewHolder> {

    private ArrayList<SevanamTypes> sevanamTypes;
    private Context context;
    private static final String LOG = "camprequirementtypesadapter";

    public CampsRequirementTypesAdapter(Context context, ArrayList<SevanamTypes> sevanamTypes) {
        this.context = context;
        this.sevanamTypes = sevanamTypes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sevanamtypes_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_type.setText(sevanamTypes.get(position).getSevanam_type());

        String img_url = sevanamTypes.get(position).getSevanam_type_url();
        int resID = context.getResources().getIdentifier(img_url, "drawable", context.getPackageName());
        Picasso.get().load(resID)
                .into(holder.img_category);

    }

    @Override
    public int getItemCount() {
        return sevanamTypes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_type;
        ImageView img_category;
        public final View mView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            tv_type = (TextView)view.findViewById(R.id.tv_type);
            img_category = (ImageView)view.findViewById(R.id.img_type);
        }
    }
}
