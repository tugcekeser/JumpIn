package com.example.tuze.bluecollar.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.activities.ApplicantsActivity;
import com.example.tuze.bluecollar.constants.AppConstants;
import com.example.tuze.bluecollar.model.Application;
import com.example.tuze.bluecollar.model.Position;
import com.example.tuze.bluecollar.model.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcel;
import org.parceler.Parcels;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Position application = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_applications, parent, false);
        }

        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);

        tvName.setText(application.getTitle());
        tvDescription.setText(application.getCompanyName());

        Picasso.with(getContext()).load(application.getImageLink())
                //.bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(getContext(),3,3))
                .into(ivImage);

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

}
