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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;


public class FirebaseServiceRequestViewHolder extends RecyclerView.ViewHolder{
    private static final String LOG = "viewholderlog";
    View mView;
    Context mContext;
    private DatabaseReference ref_identifyphonecall;

    public FirebaseServiceRequestViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();

    }


    public void bindRequest(final ServiceRequest serviceRequest) {

        TextView personname = (TextView) mView.findViewById(R.id.personname);
        personname.setText(serviceRequest.getName());

        TextView address = (TextView) mView.findViewById(R.id.address);
        address.setText(serviceRequest.getCurrentaddress());

        TextView phoneno = (TextView) mView.findViewById(R.id.personphone);
        phoneno.setText(serviceRequest.getPhonenumber());

        ImageView phone_numbercall = (ImageView) mView.findViewById(R.id.phone_number);
        ImageView mapView = (ImageView) mView.findViewById(R.id.iv_map);

        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,WhoRequireServiceMapActivity.class);
                //intent.putExtra("phonenumber",serviceRequest.getPhonenumber());
                intent.putExtra("serviceRequest", serviceRequest);
                mContext.startActivity(intent);
            }
        });
        phone_numbercall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no = serviceRequest.getPhonenumber();
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                ref_identifyphonecall = KeralathodoppamDBUtil.getInstance().getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.IDENTIFYPHONECALLS).child(KeralathodoppamConstants.WHOREQUIRESEVANAM).push();
                IdentifyPhoneCall phonecall = new IdentifyPhoneCall();
                phonecall.setCallfrom(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                phonecall.setCallto(phone_no);
                phonecall.setTimestamp(ts);
                ref_identifyphonecall.setValue(phonecall);

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
