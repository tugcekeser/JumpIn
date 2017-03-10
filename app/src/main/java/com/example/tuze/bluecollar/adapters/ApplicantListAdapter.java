package com.example.tuze.bluecollar.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.util.List;

/**
 * Created by tugce.
 */

public class ApplicantListAdapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> applicants;

    public ApplicantListAdapter(Context context, List<User> applicants,User user) {
        super(context, 0, applicants);
        this.context=context;
        this.applicants=applicants;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User applicant = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_applications, parent, false);
        }

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);

        tvName.setText(applicant.getName());
        tvDescription.setText(applicant.getTitle());

        Picasso.with(getContext()).load(applicant.getProfileImage())
                //.bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(getContext(),3,3))
                .into(ivImage);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProfileActivity.class);
                intent.putExtra(AppConstants.USER, Parcels.wrap(applicant));
                intent.putExtra(AppConstants.SCREEN_TYPE,AppConstants.APPLICANT_PROFILE);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
