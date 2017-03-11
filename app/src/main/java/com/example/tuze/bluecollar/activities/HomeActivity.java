package com.example.tuze.bluecollar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.daprlabs.cardstack.SwipeDeck;
import com.example.tuze.bluecollar.adapters.ApplicantListAdapter;
import com.example.tuze.bluecollar.adapters.ApplicantsSwipeDeckAdapter;
import com.example.tuze.bluecollar.adapters.ApplicationsAdapter;
import com.example.tuze.bluecollar.adapters.SwipeDeckAdapter;
import com.example.tuze.bluecollar.constants.AppConstants;
import com.example.tuze.bluecollar.constants.FirebaseConstants;
import com.example.tuze.bluecollar.model.Application;
import com.example.tuze.bluecollar.model.Position;
import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.model.User;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.navdrawer.SimpleSideDrawer;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    @BindView(R.id.swipeDeck)
    SwipeDeck cardStack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawer;
    @BindView(R.id.navView)
    NavigationView navigationView;
    ListView lvSavedItems;
    CircleImageView ivProfile;
    TextView tvName;
    TextView tvEmail;
    TextView tvSavedJobsApplicantsCount;
    TextView tvApplicationsCount;
    TextView tvApplications;
    TextView tvSavedJobsApplicants;
    private SimpleSideDrawer rightDrawer;
    private SwipeDeckAdapter positionsAdapter;
    private ApplicantsSwipeDeckAdapter jobSeekerAdapter;
    private ArrayList<Position> positions;
    private ArrayList<User> jobSeekers;
    private User user;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_homescreen);
        positions = new ArrayList<Position>();
        jobSeekers = new ArrayList<User>();

        //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.login_background));

        user = (User) Parcels.unwrap(getIntent().getParcelableExtra(AppConstants.USER));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        rightDrawer = new SimpleSideDrawer(this);
        rightDrawer.setRightBehindContentView(R.layout.saved_item_list);
        lvSavedItems = (ListView) findViewById(R.id.lvSavedItems);

        //Set header views
        View header = navigationView.getHeaderView(0);
        setViews(header, user);

        if (user.getType() == 1) {
            navigationView.inflateMenu(R.menu.job_seeker_menu);
            DatabaseReference mListItemRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.POSITIONS);

            mListItemRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Position position = postSnapshot.getValue(Position.class);
                        positions.add(position);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, getString(R.string.onCancelled), databaseError.toException());
                }

            });
            positionsAdapter = new SwipeDeckAdapter(this, positions, user);
            cardStack.setAdapter(positionsAdapter);

        } else {
            navigationView.inflateMenu(R.menu.employeer_menu);
            DatabaseReference mListItemRef = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.USER);

            mListItemRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        User seeker = postSnapshot.getValue(User.class);
                        if (seeker.getType() == 1)
                            jobSeekers.add(seeker);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, getString(R.string.onCancelled), databaseError.toException());
                }

            });
            jobSeekerAdapter = new ApplicantsSwipeDeckAdapter(this, jobSeekers, user);
            cardStack.setAdapter(jobSeekerAdapter);

        }
        cardStack.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + position);
            }

            @Override
            public void cardSwipedRight(int position) {
                if (user.getType() == 1) {
                    String key = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.USER)
                            .child(user.getUserId()).child("savedJobs").push().getKey();
                    FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.USER)
                            .child(user.getUserId()).child("savedJobs").child(key).setValue(positions.get(position).getPositionReference());
                } else {
                    String key = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.USER)
                            .child(user.getUserId()).child("savedApplicants").push().getKey();
                    FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.USER)
                            .child(user.getUserId()).child("savedApplicants").child(key).setValue(jobSeekers.get(position).getUserId());
                }
                Log.i("MainActivity", "card was swiped right, position in adapter: " + position);
            }

            @Override
            public void cardsDepleted() {
                Log.i("MainActivity", "no more cards");
            }

            @Override
            public void cardActionDown() {
                Log.i("MainActivity", "card was swiped down, position in adapter: ");
            }

            @Override
            public void cardActionUp() {
                Log.i("MainActivity", "card was swiped up, position in adapter: ");
            }
        });
    }

    private void setViews(View header, final User user) {
        ivProfile = (CircleImageView) header.findViewById(R.id.ivProfile);
        tvName = (TextView) header.findViewById(R.id.tvName);
        tvEmail = (TextView) header.findViewById(R.id.tvEmail);
        tvApplicationsCount = (TextView) header.findViewById(R.id.tvApplicationsCount);
        tvApplications = (TextView) header.findViewById(R.id.tvApplications);
        tvSavedJobsApplicants = (TextView) header.findViewById(R.id.tvSavedJobsApplicants);
        tvSavedJobsApplicantsCount = (TextView) header.findViewById(R.id.tvSavedJobsApplicantsCount);

        Picasso.with(this).load(user.getProfileImage()).into(ivProfile);
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        if (user.getType() == 1) {
            tvSavedJobsApplicants.setText("Saved Jobs");
            tvApplications.setText("My Applications");

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query query = ref.child("applications").orderByChild("userId").equalTo(user.getEmail());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final ArrayList<Position> positions = new ArrayList<Position>();
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
                                tvApplicationsCount.setText("" + positions.size());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });


            Query querySavedItems = ref.child("user").orderByChild("userId").equalTo(user.getUserId());
            querySavedItems.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        tvSavedJobsApplicantsCount.setText("" + userSnapshot.child("savedJobs").getChildrenCount());

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });

        } else {
            tvSavedJobsApplicants.setText("Saved Applicants");
            tvApplications.setText("Created Jobs");
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query queryPosition = ref.child("positions").orderByChild("companyName").equalTo(user.getName());
            queryPosition.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Position> positions = new ArrayList<Position>();

                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                        Position position = userSnapshot.getValue(Position.class);
                        if (position.getCompanyName().equals(user.getName())) {
                            positions.add(position);
                        }
                    }
                    tvApplicationsCount.setText("" + positions.size());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });

            Query querySavedItems = ref.child("user").orderByChild("userId").equalTo(user.getUserId());
            querySavedItems.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        tvSavedJobsApplicantsCount.setText("" + userSnapshot.child("savedApplicants").getChildrenCount());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_list) {
            if (user.getType() == 1) {
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final List<Position> positions = new ArrayList<Position>();


                Query query = ref.child(FirebaseConstants.USER).orderByChild("userId").equalTo(user.getUserId());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot dSnapshot : snapshot.child("savedJobs").getChildren()) {

                                Query query = ref.child(FirebaseConstants.POSITIONS).orderByChild("positionReference").equalTo(dSnapshot.getValue().toString());
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Position position = snapshot.getValue(Position.class);
                                            positions.add(position);
                                        }
                                        ApplicationsAdapter adapter = new ApplicationsAdapter(HomeActivity.this, positions, user);
                                        lvSavedItems.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e(TAG, getString(R.string.onCancelled), databaseError.toException());
                                    }
                                });
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, getString(R.string.onCancelled), databaseError.toException());
                    }
                });
            } else if (user.getType() == 2) {
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final List<User> applicants = new ArrayList<User>();


                Query query = ref.child(FirebaseConstants.USER).orderByChild("email").equalTo(user.getEmail());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (DataSnapshot dSnapshot : snapshot.child("savedApplicants").getChildren()) {

                                Query query2 = ref.child(FirebaseConstants.USER).orderByChild("userId").equalTo(dSnapshot.getValue().toString());
                                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            User applicant = snapshot.getValue(User.class);
                                            applicants.add(applicant);
                                        }
                                        ApplicantListAdapter adapter = new ApplicantListAdapter(HomeActivity.this, applicants, user, new ArrayList<Application>());
                                        lvSavedItems.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e(TAG, getString(R.string.onCancelled), databaseError.toException());
                                    }
                                });
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, getString(R.string.onCancelled), databaseError.toException());
                    }
                });
            }


            rightDrawer.toggleRightDrawer();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            intent = new Intent(this, HomeActivity.class).putExtra(AppConstants.USER, Parcels.wrap(user));
            startActivity(intent);

        } else if (id == R.id.nav_profile) {
            intent = new Intent(this, ProfileActivity.class).putExtra(AppConstants.USER, Parcels.wrap(user));
            intent.putExtra(AppConstants.SCREEN_TYPE, AppConstants.PROFILE);
            startActivity(intent);

        } else if (id == R.id.nav_create_job) {
            intent = new Intent(this, CreateJobPostActivity.class).putExtra(AppConstants.USER, Parcels.wrap(user));
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {
            /*FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);*/
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
