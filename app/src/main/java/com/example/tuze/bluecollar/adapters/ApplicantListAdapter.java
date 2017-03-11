package com.example.tuze.bluecollar.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.activities.ProfileActivity;
import com.example.tuze.bluecollar.constants.AppConstants;
import com.example.tuze.bluecollar.constants.FirebaseConstants;
import com.example.tuze.bluecollar.constants.JobStatus;
import com.example.tuze.bluecollar.model.Application;
import com.example.tuze.bluecollar.model.User;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by tugce.
 */

public class ApplicantListAdapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> applicants;
    private List<Application> applications;

    public ApplicantListAdapter(Context context, List<User> applicants, User user, List<Application> applications) {
        super(context, 0, applicants);
        this.context = context;
        this.applicants = applicants;
        this.applications = applications;
    }
    private static class ViewHolderApplicant{
        ImageView ivImage;
        TextView tvName;
        TextView tvDescription;
        Spinner sStatus;
        ImageButton btnView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final User applicant = getItem(position);
        final ViewHolderApplicant holder=new ViewHolderApplicant();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_applications, parent, false);
        }

        holder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
        holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
        holder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        holder.sStatus = (Spinner) convertView.findViewById(R.id.sJobStatus);
        holder.btnView = (ImageButton) convertView.findViewById(R.id.btnView);

        holder.tvName.setText(applicant.getName());
        holder.tvDescription.setText(applicant.getTitle());
        holder.btnView.setVisibility(View.GONE);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(context, R.array.Status, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        if (applications.size() > 0) {
            holder.sStatus.setAdapter(adapter);
            holder.sStatus.setSelection(applications.get(position).getStatus() - 1);
        } else
            holder.sStatus.setVisibility(View.GONE);

        holder.sStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                String selection = holder.sStatus.getSelectedItem().toString();
                /*Update db*/
                int statusCode = JobStatus.getStatusCode(selection);
                FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.APPLICATIONS)
                        .child(applications.get(position).getApplicationReference()).child("status").setValue(statusCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                String selection = holder.sStatus.getSelectedItem().toString();
                Toast.makeText(context, "Try Again " + selection, Toast.LENGTH_LONG).show();
            }
        });

        Picasso.with(getContext()).load(applicant.getProfileImage())
                //.bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(getContext(),3,3))
                .into(holder.ivImage);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(AppConstants.USER, Parcels.wrap(applicant));
                intent.putExtra(AppConstants.SCREEN_TYPE, AppConstants.APPLICANT_PROFILE);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
