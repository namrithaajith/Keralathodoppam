package com.mobioetech.keralathodoppam.keralathodoppam;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ServicePersonsAdapter extends RecyclerView.Adapter<ServicePersonsAdapter.ViewHolder> {
    //private ArrayList<String> phonenumbers;
    private ArrayList<Person> person_al;
    private Context context;
    private static final String LOG = "sevanamAdapter";
    private DatabaseReference ref_identifyphonecall;
    public ServicePersonsAdapter(Context context, ArrayList<Person> person) {

        this.context = context;
        this.person_al = person;
    }

    @NonNull
    @Override
    public ServicePersonsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.servicepersons_list_item, parent, false);
        return new ServicePersonsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicePersonsAdapter.ViewHolder holder, final int position) {
        if(person_al.get(position)!= null){

            holder.tv_phone.setText(person_al.get(position).getPhonenumber());
            holder.tv_name.setText(person_al.get(position).getName());
            holder.tv_service.setText(person_al.get(position).getServicetype());
            if(person_al.get(position).getLocality().equals(null)){
                holder.tv_place.setText(person_al.get(position).getDistrict());
            }
            else{
                holder.tv_place.setText(person_al.get(position).getLocality() + ","+person_al.get(position).getDistrict());
            }

            holder.tv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context,ServiceDetailView.class);
                    intent.putExtra("phonenumber",person_al.get(position).getPhonenumber());
                    context.startActivity(intent);
                }
            });
            holder.btn_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Long tsLong = System.currentTimeMillis()/1000;
                    String ts = tsLong.toString();

                    ref_identifyphonecall = KeralathodoppamDBUtil.getInstance().getReference().child(KeralathodoppamConstants.KERALA).child(KeralathodoppamConstants.IDENTIFYPHONECALLS).child(KeralathodoppamConstants.SEVANAM).push();
                    IdentifyPhoneCall phonecall = new IdentifyPhoneCall();
                    phonecall.setCallfrom(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                    phonecall.setCallto(person_al.get(position).getPhonenumber());
                    phonecall.setTimestamp(ts);
                    ref_identifyphonecall.setValue(phonecall);

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+person_al.get(position).getPhonenumber()));
                    Log.i(LOG,"Phone number------>"+person_al.get(position).getPhonenumber());
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                                Manifest.permission.CALL_PHONE)) {
                            new AlertDialog.Builder(context)
                                    .setTitle(context.getResources().getString(R.string.permission))
                                    .setMessage(context.getResources().getString(R.string.permission_callphone))
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            //re-request
                                            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE},
                                                    2);
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                        else{
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{Manifest.permission.CALL_PHONE},2);
                        }

                    }
                    else{
                        context.startActivity(callIntent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return person_al.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_phone;
        TextView tv_name;
        TextView tv_place;
        TextView tv_service;
        ImageView btn_call;
        public final View mView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            tv_name = (TextView)view.findViewById(R.id.personname);
            tv_phone = (TextView)view.findViewById(R.id.personphone);
            tv_place = (TextView)view.findViewById(R.id.place);
            btn_call = (ImageView)view.findViewById(R.id.btn_call);
            tv_service = (TextView) view.findViewById(R.id.servicetype);
        }
    }
}
