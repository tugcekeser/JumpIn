package com.example.tuze.bluecollar.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.activities.HomeActivity;
import com.example.tuze.bluecollar.activities.ProfileActivity;
import com.example.tuze.bluecollar.constants.AppConstants;
import com.example.tuze.bluecollar.model.Application;
import com.example.tuze.bluecollar.model.Position;
import com.example.tuze.bluecollar.model.User;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tugce.
 */

public class ApplicantListAdapter extends RecyclerView.Adapter<ApplicantListAdapter.ApplicantViewHolder> {

    ArrayList<User> applicants;
    User user;
    Context mContext;

    public ApplicantListAdapter(Context context, ArrayList<User> p, User user) {
        mContext = context;
        applicants = p;
        this.user = user;
    }

    @Override
    public ApplicantListAdapter.ApplicantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.applicant_item, parent, false);
        ApplicantListAdapter.ApplicantViewHolder vh = new ApplicantListAdapter.ApplicantViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ApplicantListAdapter.ApplicantViewHolder holder, int position) {

        holder.tvTitle.setText(applicants.get(position).getTitle());
        holder.tvLocation.setText(applicants.get(position).getAddress());
        holder.tvName.setText(applicants.get(position).getName());
        Picasso.with(mContext).load(applicants.get(position).getProfileImage()).placeholder(R.drawable.avatar_user).into(holder.ivUserImage);

    }

    @Override
    public int getItemCount() {
        return applicants.size();
    }

    class ApplicantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivUserImage;
        TextView tvTitle;
        TextView tvName;
        TextView tvLocation;


        public ApplicantViewHolder(View itemView) {
            super(itemView);
            ivUserImage = (ImageView) itemView.findViewById(R.id.ivUserImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvApplicantTitle);
            tvName = (TextView) itemView.findViewById(R.id.tvApplicantName);
            //tvLocation=(TextView)itemView.findViewById(R.id.tvApplicantLocation);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            Intent intent = new Intent(mContext, ProfileActivity.class);
            intent.putExtra(AppConstants.USER, Parcels.wrap(applicants.get(position)));
            intent.putExtra(AppConstants.SCREEN_TYPE, AppConstants.APPLICANT_PROFILE);
            mContext.startActivity(intent);
        }

    }
}
