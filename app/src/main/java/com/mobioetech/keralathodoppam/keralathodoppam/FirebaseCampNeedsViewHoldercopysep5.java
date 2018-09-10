package com.mobioetech.keralathodoppam.keralathodoppam;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FirebaseCampNeedsViewHoldercopysep5 extends RecyclerView.ViewHolder{
    private static final String LOG = "viewholderlog";
    View mView;
    Context mContext;

    public FirebaseCampNeedsViewHoldercopysep5(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();

    }


    public void bindCampNeeds(final CampNeeds campNeeds) {

        TextView name = (TextView) mView.findViewById(R.id.campname);
        name.setText(campNeeds.getCampname());

        TextView address = (TextView) mView.findViewById(R.id.address);
        if(campNeeds.getLocality() == null){
            address.setText(campNeeds.getDistrict());
        }
        else{
            address.setText(campNeeds.getLocality()+" "+campNeeds.getDistrict());
        }


        TextView phoneno = (TextView) mView.findViewById(R.id.personphone);
        phoneno.setText(campNeeds.getPhonenumber());

        ImageView phone_numbercall = (ImageView) mView.findViewById(R.id.phone_number);

        TextView needs = (TextView) mView.findViewById(R.id.campneeds);

        needs.setText(mContext.getResources().getString(R.string.needs)+":\n"+campNeeds.getNeeds());

        ImageView needsImage = (ImageView) mView.findViewById(R.id.imv_needs);

        Log.i(LOG,"campNeeds.getNeeds_url()=-------->"+campNeeds.getNeeds_url());
        if(campNeeds.getNeeds_url() != null){

            Picasso.get()
                    .load(campNeeds.getNeeds_url())
                    .resize(600, 600)
                    .centerCrop()
                    .into(needsImage);
        }


        phone_numbercall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no = campNeeds.getPhonenumber();
                if(phone_no != null)
                {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+phone_no));
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                                Manifest.permission.CALL_PHONE)) {
                            new AlertDialog.Builder(mContext)
                                    .setTitle(mContext.getResources().getString(R.string.permission))
                                    .setMessage(mContext.getResources().getString(R.string.permission_callphone))
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //re-request
                                            ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.CALL_PHONE},
                                                    2);
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                        else{
                            ActivityCompat.requestPermissions((Activity) mContext,
                                    new String[]{Manifest.permission.CALL_PHONE},2);
                        }

                    }
                    else{
                        mContext.startActivity(callIntent);
                    }
                }

            }
        });


        }



}
