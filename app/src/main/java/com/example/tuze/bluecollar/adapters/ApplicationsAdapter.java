package com.example.tuze.bluecollar.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.activities.ApplicantsActivity;
import com.example.tuze.bluecollar.activities.ApplicationsActivity;
import com.example.tuze.bluecollar.activities.HomeActivity;
import com.example.tuze.bluecollar.constants.AppConstants;
import com.example.tuze.bluecollar.constants.FirebaseConstants;
import com.example.tuze.bluecollar.constants.JobStatus;
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

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.tuze.bluecollar.R.drawable.user;

/**
 * Created by tuze on 3/9/17.
 */

public class ApplicationsAdapter extends ArrayAdapter<Position> {
    private User user;

    public ApplicationsAdapter(Context context, List<Position> applications, User user) {
        super(context, 0, applications);
        this.user = user;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Position application = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_applications, parent, false);
        }

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        Spinner sStatus=(Spinner) convertView.findViewById(R.id.sJobStatus);

        sStatus.setVisibility(View.GONE);
        tvName.setText(application.getTitle());
        tvDescription.setText(application.getCompanyName());

        Picasso.with(getContext()).load(application.getImageLink())
                //.bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(getContext(),3,3))
                .into(ivImage);

        if(user.getType()==1){
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayDetail(application);
                }
            });

        }

        if (user.getType() == 2) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), ApplicantsActivity.class)
                            .putExtra(AppConstants.USER, Parcels.wrap(user));
                    i.putExtra("Position", Parcels.wrap(application));
                    getContext().startActivity(i);
                }
            });
        }
        return convertView;
    }

    private void displayDetail(Position position) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View getListItemView = li.inflate(R.layout.dialog_display_status, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setView(getListItemView);

        final SeekBar sbStatus = (SeekBar) getListItemView.findViewById(R.id.sbStatus);
        final TextView tvStatus = (TextView) getListItemView.findViewById(R.id.tvStatus);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("applications").orderByChild("positionReference").equalTo(position.getPositionReference());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Application application = snapshot.getValue(Application.class);
                    tvStatus.setText(JobStatus.getStatus(application.getStatus()));
                    sbStatus.setMax(2);
                    sbStatus.setClickable(false);
                    sbStatus.setFocusable(false);
                    sbStatus.setEnabled(false);
                    switch(application.getStatus()){
                        case 1:
                            sbStatus.setProgress(0);
                            tvStatus.setTextColor(Color.MAGENTA);
                            break;
                        case 2:
                            sbStatus.setProgress(1);
                            tvStatus.setTextColor(Color.GREEN);
                            break;
                        case 3:
                            sbStatus.setProgress(2);
                            tvStatus.setTextColor(Color.BLUE);
                            break;
                        case 4:
                            sbStatus.setProgress(2);
                            tvStatus.setTextColor(Color.RED);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
             //   Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        // set dialog message
        alertDialogBuilder.create().show();
    }

}
