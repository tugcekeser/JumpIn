package com.example.tuze.bluecollar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.adapters.PhotosCardAdapter;
import com.example.tuze.bluecollar.constants.AppConstants;
import com.example.tuze.bluecollar.constants.FirebaseConstants;
import com.example.tuze.bluecollar.fragments.ComposeMessageFragment;
import com.example.tuze.bluecollar.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProfileActivity";
    private User user;
    private PhotosCardAdapter photosCardAdapter;
    /*private ArrayList<Position> positions;
    private ArrayList<User> applicants;*/
    private ArrayList<String> photoLinkList;
    private String screenType;
    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;
    @BindView(R.id.ivProfileImageSmall)
    CircleImageView ivProfileImageSmall;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.btnApplications)
    Button btnApplications;
    @BindView(R.id.tvMediaTitle)
    TextView tvMediaTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        user=(User) Parcels.unwrap(intent.getParcelableExtra(AppConstants.USER));
        getSupportActionBar().setTitle(user.getName());

        screenType = intent.getStringExtra(AppConstants.SCREEN_TYPE);
        btnApplications.setOnClickListener(this);

        if (screenType.equals(AppConstants.PROFILE)) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.edit));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, getString(R.string.under_the_construction), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.action), null).show();
                }
            });
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.contact));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditDialog();
                }
            });
            btnApplications.setVisibility(View.GONE);
        }

        Picasso.with(this).load(user.getProfileImage()).transform(new BlurTransformation(this, 5)).into(ivProfileImage);
        Picasso.with(this).load(user.getProfileImage()).into(ivProfileImageSmall);


        tvDescription.setText("\"" + user.getDescription() + "\"");
        tvTitle.setText(user.getTitle() + ", " + user.getAddress());
        photoLinkList = new ArrayList<String>();


        final DatabaseReference refPhotos = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.USER).child(user.getUserId()).child(FirebaseConstants.PHOTOS);
        refPhotos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String value = postSnapshot.getValue().toString();
                    photoLinkList.add(value);
                }
                photosCardAdapter = new PhotosCardAdapter(ProfileActivity.this, photoLinkList);
                rvPhotos.setAdapter(photosCardAdapter);
                GridLayoutManager gridLayoutManager =
                        new GridLayoutManager(ProfileActivity.this, 3);
                rvPhotos.setLayoutManager(gridLayoutManager);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, getString(R.string.onCancelled), databaseError.toException());
            }

        });

        if(user.getType()==2){
            tvMediaTitle.setText("Workplace Images");
        }


    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeMessageFragment composeTweetDialogFragment = ComposeMessageFragment.newInstance(getString(R.string.message), user, user);
        composeTweetDialogFragment.show(fm, getString(R.string.message));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.btnApplications && user.getType()==1){
            Intent intent=new Intent(this,ApplicationsActivity.class).putExtra(AppConstants.USER, Parcels.wrap(user));
            startActivity(intent);
        }
        else if(id==R.id.btnApplications && user.getType()==2){
            Intent intent=new Intent(this,UserCreatedJobsActivity.class).putExtra(AppConstants.USER, Parcels.wrap(user));
            startActivity(intent);
        }
    }
}
