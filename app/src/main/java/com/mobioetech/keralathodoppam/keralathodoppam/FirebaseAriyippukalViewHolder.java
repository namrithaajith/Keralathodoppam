package com.mobioetech.keralathodoppam.keralathodoppam;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FirebaseAriyippukalViewHolder extends RecyclerView.ViewHolder{
    private static final String LOG = "viewholderlog";
    View mView;
    Context mContext;


    public FirebaseAriyippukalViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();

    }


    public void bindAriyippukal(final Ariyippukal ariyippukal) {

        TextView name = (TextView) mView.findViewById(R.id.tv_heading);
        name.setText(ariyippukal.getHeading());

        ImageView iv_thumbnail = (ImageView) mView.findViewById(R.id.thumbnail);

        String thumbnail = ariyippukal.getImage();

        if(thumbnail != null) {
            Picasso.get()
                    .load(thumbnail)
                    .into(iv_thumbnail);
        }

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ariyippukal.getDetails() == null){
                    if(ariyippukal.getYoutubeLink() != null){
                        String url = ariyippukal.getYoutubeLink();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        mContext.startActivity(i);
                    }
                    }
                    else{
                    Intent intent = new Intent(mContext,AriyipukalDetailActivity.class);
                    intent.putExtra("ariyippukal",ariyippukal);
                    mContext.startActivity(intent);
                }

            }
        });

        }

}
