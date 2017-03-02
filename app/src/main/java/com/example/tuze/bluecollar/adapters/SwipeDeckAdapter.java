package com.example.tuze.bluecollar.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.activities.HomeActivity;
import com.example.tuze.bluecollar.constants.AppConstants;
import com.example.tuze.bluecollar.constants.FirebaseConstants;
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

public class SwipeDeckAdapter extends BaseAdapter {

    ArrayList<Position> positions;
    User user;
    Context mContext;

    public SwipeDeckAdapter(Context context, ArrayList<Position> p, User user) {
        mContext = context;
        positions = p;
        this.user = user;
    }

    @Override
    public int getCount() {
        return positions.size();
    }

    @Override
    public Object getItem(int position) {
        return positions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.item_position, parent, false);

        ImageView ivUserImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvPositionName);
        TextView tvCompanyName = (TextView) convertView.findViewById(R.id.tvCompanyName);
        TextView tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);

        tvTitle.setText(positions.get(position).getTitle());
        tvLocation.setText(positions.get(position).getLocation());
        tvCompanyName.setText(positions.get(position).getCompanyName());
        Picasso.with(mContext).load(positions.get(position).getImageLink()).placeholder(R.drawable.bluecollar).into(ivUserImage);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // gets item position
                displayDetail(positions.get(position));
            }
        });

        return convertView;
    }

    private void displayDetail(Position position) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View getListItemView = li.inflate(R.layout.dialog_display_position, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        alertDialogBuilder.setView(getListItemView);

        final TextView tvPositionName = (TextView) getListItemView.findViewById(R.id.tvPositionName);
        final TextView tvCompanyName = (TextView) getListItemView.findViewById(R.id.tvCompanyName);
        final TextView tvLocation = (TextView) getListItemView.findViewById(R.id.tvLocation);
        final TextView tvDeadline = (TextView) getListItemView.findViewById(R.id.tvDeadline);
        final TextView tvJobDescription = (TextView) getListItemView.findViewById(R.id.tvJobDescription);

        final Button btnApply = (Button) getListItemView.findViewById(R.id.btnApply);

        tvPositionName.setText(position.getTitle());
        tvJobDescription.setText(position.getDescription());
        tvLocation.setText(position.getLocation());
        tvCompanyName.setText(position.getCompanyName());
        tvDeadline.setText(position.getDeadline());

        DateFormat dateFormat = new SimpleDateFormat(AppConstants.SIMPLE_DATE_FORMAT);
        Date date = new Date();

        final Application application = new Application();
        application.setApplicationDate(dateFormat.format(date).toString());
        application.setPositionReference(position.getPositionReference());
        application.setUserId(user.getEmail());

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String key = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.APPLICATIONS).push().getKey();
                FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.APPLICATIONS).child(key).setValue(application);
                mContext.startActivity(new Intent(mContext, HomeActivity.class).putExtra(AppConstants.USER, Parcels.wrap(user)));
            }
        });

        // set dialog message
        alertDialogBuilder.create().show();
    }
}