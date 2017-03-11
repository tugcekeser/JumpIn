package com.example.tuze.bluecollar.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.tuze.bluecollar.activities.UserCreatedJobsActivity;
import com.example.tuze.bluecollar.constants.AppConstants;
import com.example.tuze.bluecollar.model.Application;
import com.example.tuze.bluecollar.model.Position;
import com.example.tuze.bluecollar.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by tugce.
 */

public class ApplicantsSwipeDeckAdapter extends BaseAdapter {
    private static final String TAG = "ApplicantsSDeckAdapter";

    ArrayList<User> applicants;
    User user;
    Context mContext;
    PhotosCardAdapter photosCardAdapter;

    public ApplicantsSwipeDeckAdapter(Context context, ArrayList<User> p, User user) {
        mContext = context;
        applicants = p;
        this.user = user;
    }

    private static class ViewHolderApplicant {
        ImageView ivUserImage;
        TextView tvTitle;
        TextView tvName;
        TextView tvLookingFor;
        RecyclerView rvPhotos;

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
        final ViewHolderApplicant holder=new ViewHolderApplicant();
        final ArrayList<String> photoLinkList=new ArrayList<String>();

        holder.ivUserImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
        holder.tvTitle=(TextView) convertView.findViewById(R.id.tvApplicantTitle);
        holder.tvName=(TextView)convertView.findViewById(R.id.tvApplicantName);
        holder.tvLookingFor=(TextView)convertView.findViewById(R.id.tvLookingFor);
        holder.rvPhotos=(RecyclerView)convertView.findViewById(R.id.rvPhotos);

        Query queryPosition = FirebaseDatabase.getInstance().getReference().child("user").orderByChild("userId").equalTo(applicants.get(position).getUserId());
        queryPosition.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for(DataSnapshot dSnapshot: snapshot.child("photos").getChildren()){
                        String link=dSnapshot.getValue().toString();
                        photoLinkList.add(link);
                    }
                    GridLayoutManager gridLayoutManager =
                            new GridLayoutManager(mContext,4);
                    holder.rvPhotos.setLayoutManager(gridLayoutManager);
                    photosCardAdapter=new PhotosCardAdapter(mContext,photoLinkList);
                    holder.rvPhotos.setAdapter(photosCardAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });


       /* photoLinkList.add("https://s20.postimg.org/p02mtq119/image.jpg");
        photoLinkList.add("https://s20.postimg.org/e1rdbjcfx/image.jpg");
        photoLinkList.add("https://s20.postimg.org/kthsee1fh/image.jpg");
        photoLinkList.add("https://s20.postimg.org/nouvl95fh/image.jpg");
        photoLinkList.add("https://s20.postimg.org/dp4kj3jjx/image.jpg");
        photoLinkList.add("https://s20.postimg.org/n0m12b6pp/d15.jpg");
        photoLinkList.add("https://s20.postimg.org/8ldauw8fx/d11.jpg");
        photoLinkList.add("https://s20.postimg.org/d5zh9ts59/d12.jpg");*/

        holder.tvTitle.setText(applicants.get(position).getTitle()+", "+applicants.get(position).getAddress());
        holder.tvLookingFor.setText(AppConstants.LOOKING_FOR+applicants.get(position).getLookingFor());
        holder.tvName.setText(applicants.get(position).getName());
        Picasso.with(mContext).load(applicants.get(position).getProfileImage()).placeholder(R.drawable.avatar_user).into(holder.ivUserImage);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ProfileActivity.class);
                intent.putExtra(AppConstants.USER, Parcels.wrap(applicants.get(position)));
                intent.putExtra(AppConstants.SCREEN_TYPE,AppConstants.APPLICANT_PROFILE);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
