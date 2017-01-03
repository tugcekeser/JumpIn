package com.example.tuze.bluecollar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.adapters.ApplicantListAdapter;
import com.example.tuze.bluecollar.adapters.PhotosCardAdapter;
import com.example.tuze.bluecollar.adapters.PositionsAdapter;
import com.example.tuze.bluecollar.fragments.ComposeMessageFragment;
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


import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private User user;
    private RecyclerView rvApplicationList;
    private RecyclerView rvPhotos;
    private PhotosCardAdapter photosCardAdapter;
    private PositionsAdapter adapterPosition;
    private ApplicantListAdapter adapterApplicant;
    private ArrayList<Position> positions;
    private ArrayList<User> applicants;
    private ArrayList<String> photoLinkList;
    private String screenType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        user = (User) intent.getSerializableExtra("User");
        getSupportActionBar().setTitle(user.getName());

        screenType=intent.getStringExtra("ScreenType");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(screenType.equals("Profile")) {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.edit));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Under the construction...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }
        else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.contact));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   showEditDialog();
                }
            });
        }

        TextView tvEmail=(TextView) findViewById(R.id.tvEmail);
        TextView tvAddress=(TextView)findViewById(R.id.tvAddress);
        TextView tvDescription=(TextView)findViewById(R.id.tvDescription);
        ImageView ivProfileImage=(ImageView)findViewById(R.id.ivProfileImage);
        TextView tvTitle=(TextView)findViewById(R.id.tvTitle);
        TextView tvLookigFor=(TextView)findViewById(R.id.tvLookingFor);
        Picasso.with(this).load(user.getProfileImage()).into(ivProfileImage);

        tvEmail.setText(user.getEmail());
        tvAddress.setText(user.getAddress());
        tvDescription.setText(user.getDescription());
        tvLookigFor.setText(user.getLookingFor());
        tvTitle.setText(user.getTitle());

        rvApplicationList=(RecyclerView)findViewById(R.id.rvApplicationList);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);
        rvApplicationList.setLayoutManager(linearLayoutManager);
        rvPhotos=(RecyclerView)findViewById(R.id.rvPhotos);
        positions=new ArrayList<Position>();
        applicants=new ArrayList<User>();
        photoLinkList=new ArrayList<String>();


        final DatabaseReference refPhotos=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUserId()).child("photos");
        refPhotos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String value = postSnapshot.getValue().toString();
                    photoLinkList.add(value);
                }
                photosCardAdapter=new PhotosCardAdapter(ProfileActivity.this,photoLinkList);
                rvPhotos.setAdapter(photosCardAdapter);
                GridLayoutManager gridLayoutManager =
                        new GridLayoutManager(ProfileActivity.this,2);
                rvPhotos.setLayoutManager(gridLayoutManager);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
         Query query = ref.child("applications").orderByChild("userId").equalTo(user.getEmail());
        if (user.getType()==1) {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Application application = userSnapshot.getValue(Application.class);
                        Query queryPosition = ref.child("positions").orderByChild("positionReference").equalTo(application.getPositionReference());

                        queryPosition.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    Position position = userSnapshot.getValue(Position.class);
                                    positions.add(position);
                                }
                                adapterPosition = new PositionsAdapter(ProfileActivity.this, positions, user);
                                rvApplicationList.setAdapter(adapterPosition);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else {

            DatabaseReference mListItemRef = FirebaseDatabase.getInstance().getReference().child("applications");//.getRef().child("KXQ5mRVEbFxYDPoLtiA");
            // ArrayList<Application> applicationList=new ArrayList<Application>();
            mListItemRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        final Application application = postSnapshot.getValue(Application.class);
                        Query queryPosition = ref.child("positions").orderByChild("positionReference").equalTo(application.getPositionReference());
                        queryPosition.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    Position position = userSnapshot.getValue(Position.class);
                                    if (position.getCompanyName().equals(user.getName())) {

                                        Query queryUser = ref.child("user").orderByChild("email").equalTo(application.getUserId());
                                        queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                                    User user = userSnapshot.getValue(User.class);
                                                    applicants.add(user);
                                                }
                                                adapterApplicant = new ApplicantListAdapter(ProfileActivity.this, applicants, user);
                                                rvApplicationList.setAdapter(adapterApplicant);

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }


    }
    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeMessageFragment composeTweetDialogFragment = ComposeMessageFragment.newInstance("Some Title",user,user);
        composeTweetDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}