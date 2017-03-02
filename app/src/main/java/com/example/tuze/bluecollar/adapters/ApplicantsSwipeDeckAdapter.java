package com.example.tuze.bluecollar.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.activities.HomeActivity;
import com.example.tuze.bluecollar.activities.ProfileActivity;
import com.example.tuze.bluecollar.model.Application;
import com.example.tuze.bluecollar.model.Position;
import com.example.tuze.bluecollar.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tugce.
 */

public class ApplicantsSwipeDeckAdapter extends BaseAdapter {

    ArrayList<User> applicants;
    User user;
    Context mContext;

    public ApplicantsSwipeDeckAdapter(Context context, ArrayList<User> p, User user) {
        mContext = context;
        applicants = p;
        this.user = user;
    }

    @Override
    public int getCount() {
        return applicants.size();
    }

    @Override
    public Object getItem(int position) {
        return applicants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.applicant_item, parent, false);

        ImageView ivUserImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
        TextView tvTitle=(TextView) convertView.findViewById(R.id.tvApplicantTitle);
        TextView tvName=(TextView)convertView.findViewById(R.id.tvApplicantName);
        TextView tvLookingFor=(TextView)convertView.findViewById(R.id.tvLookingFor);
        RecyclerView rvPhotos=(RecyclerView)convertView.findViewById(R.id.rvPhotos);
        ArrayList<String> photoLinkList=new ArrayList<String>();


        photoLinkList.add("https://s20.postimg.org/p02mtq119/image.jpg");
        photoLinkList.add("https://s20.postimg.org/e1rdbjcfx/image.jpg");
        photoLinkList.add("https://s20.postimg.org/kthsee1fh/image.jpg");
        photoLinkList.add("https://s20.postimg.org/nouvl95fh/image.jpg");
        photoLinkList.add("https://s20.postimg.org/dp4kj3jjx/image.jpg");
        photoLinkList.add("https://s20.postimg.org/n0m12b6pp/d15.jpg");
        photoLinkList.add("https://s20.postimg.org/8ldauw8fx/d11.jpg");
        photoLinkList.add("https://s20.postimg.org/d5zh9ts59/d12.jpg");

        PhotosCardAdapter photosCardAdapter=new PhotosCardAdapter(mContext,photoLinkList);
        rvPhotos.setAdapter(photosCardAdapter);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(mContext,4);
        rvPhotos.setLayoutManager(gridLayoutManager);


        /*final DatabaseReference refPhotos=FirebaseDatabase.getInstance().getReference().child("user").child(applicants.get(position).getUserId()).child("photos");
        refPhotos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String value = postSnapshot.getValue().toString();
                    photoLinkList.add(value);
                }
                PhotosCardAdapter photosCardAdapter=new PhotosCardAdapter(mContext,photoLinkList);
                rvPhotos.setAdapter(photosCardAdapter);
                GridLayoutManager gridLayoutManager =
                        new GridLayoutManager(mContext,3);
                rvPhotos.setLayoutManager(gridLayoutManager);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });*/

        tvTitle.setText(applicants.get(position).getTitle()+", "+applicants.get(position).getAddress());
        tvLookingFor.setText("Looking for "+applicants.get(position).getLookingFor());
        tvName.setText(applicants.get(position).getName());
        Picasso.with(mContext).load(applicants.get(position).getProfileImage()).placeholder(R.drawable.avatar_user).into(ivUserImage);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ProfileActivity.class);
                intent.putExtra("User", Parcels.wrap(applicants.get(position)));
                intent.putExtra("ScreenType","ApplicantProfile");
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
