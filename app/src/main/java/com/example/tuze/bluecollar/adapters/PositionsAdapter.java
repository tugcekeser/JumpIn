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

public class PositionsAdapter extends RecyclerView.Adapter<PositionsAdapter.PositionViewHolder> {

    ArrayList<Position> positions;
    User user;
    Context mContext;

    public PositionsAdapter(Context context, ArrayList<Position> p, User user) {
        mContext = context;
        positions = p;
        this.user = user;
    }

    @Override
    public PositionsAdapter.PositionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_position, parent, false);
        PositionsAdapter.PositionViewHolder vh = new PositionsAdapter.PositionViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(PositionsAdapter.PositionViewHolder holder, int position) {

        holder.tvTitle.setText(positions.get(position).getTitle());
        holder.tvLocation.setText(positions.get(position).getLocation());
        holder.tvCompanyName.setText(positions.get(position).getCompanyName());
        Picasso.with(mContext).load(positions.get(position).getImageLink()).placeholder(R.drawable.bluecollar).into(holder.ivUserImage);

    }

    @Override
    public int getItemCount() {
        return positions.size();
    }

    class PositionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivUserImage;
        TextView tvTitle;
        TextView tvCompanyName;
        TextView tvLocation;


        public PositionViewHolder(View itemView) {
            super(itemView);
            ivUserImage = (ImageView) itemView.findViewById(R.id.ivUserImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvPositionName);
            tvCompanyName = (TextView) itemView.findViewById(R.id.tvCompanyName);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            displayDetail(positions.get(position));
        }

        private void displayDetail(Position position){
            LayoutInflater li = LayoutInflater.from(mContext);
            View getListItemView = li.inflate(R.layout.dialog_display_position, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

            alertDialogBuilder.setView(getListItemView);

            final TextView tvPositionName = (TextView) getListItemView.findViewById(R.id.tvPositionName);
            final TextView tvCompanyName=(TextView) getListItemView.findViewById(R.id.tvCompanyName);
            final TextView tvLocation=(TextView) getListItemView.findViewById(R.id.tvLocation);
            final TextView tvDeadline=(TextView) getListItemView.findViewById(R.id.tvDeadline);
            final TextView tvJobDescription=(TextView) getListItemView.findViewById(R.id.tvJobDescription);

            final Button btnApply=(Button) getListItemView.findViewById(R.id.btnApply);

            tvPositionName.setText(position.getTitle());
            tvJobDescription.setText(position.getDescription());
            tvLocation.setText(position.getLocation());
            tvCompanyName.setText(position.getCompanyName());
            tvDeadline.setText(position.getDeadline());

            DateFormat dateFormat = new SimpleDateFormat(AppConstants.SIMPLE_DATE_FORMAT);
            Date date = new Date();

            final Application application=new Application();
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
}