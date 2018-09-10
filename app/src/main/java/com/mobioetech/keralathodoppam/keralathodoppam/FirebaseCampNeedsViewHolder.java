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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class FirebaseCampNeedsViewHolder extends RecyclerView.ViewHolder{
    private static final String LOG = "viewholderlog";
    View mView;
    Context mContext;
    String imagePath;

    public FirebaseCampNeedsViewHolder(View itemView) {
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

        imagePath = campNeeds.getImagePath();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(campNeeds.getTimestamp()) * 1000L);
        final String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();

        ImageView imvShare = (ImageView) mView.findViewById(R.id.imv_share);
        imvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG,"imagePath-----campNeeds.getNeeds()----"+imagePath+","+campNeeds.getNeeds());
                if(imagePath != null){
                    shareImage();
                }
                else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, campNeeds.getCampname());
                    String share_text = campNeeds.getCampname()+"\n"+date +"\n"+mContext.getResources().getString(R.string.needs)+" \n "+campNeeds.getNeeds();

                    sharingIntent.putExtra(Intent.EXTRA_TEXT, share_text);
                    mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getResources().getString(R.string.share_chooser)));

                }


            }
        });
        if(campNeeds.getNeeds_url() != null){

            Picasso.get()
                    .load(campNeeds.getNeeds_url())
                    .resize(600, 600)
                    .centerCrop()
                    .into(needsImage);

            needsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,CampRequirementZoomActivity.class);
                    intent.putExtra("campNeeds",campNeeds);
                    mContext.startActivity(intent);
                }
            });
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

    private void shareImage() {
        Intent share = new Intent(Intent.ACTION_SEND);

        // If you want to share a png image only, you can do:
        // setType("image/png"); OR for jpeg: setType("image/jpeg");
        share.setType("image/*");

        // Make sure you put example png image named myImage.png in your
        // directory
//        String imagePath = Environment.getExternalStorageDirectory()
//                + "/myImage.png";

        File imageFileToShare = new File(imagePath);

        Uri uri = Uri.fromFile(imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);

        mContext.startActivity(Intent.createChooser(share, "Share Image!"));
    }




}
