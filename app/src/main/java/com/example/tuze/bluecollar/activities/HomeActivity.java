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
import com.daprlabs.cardstack.SwipeDeck;
import com.example.tuze.bluecollar.adapters.ApplicantsSwipeDeckAdapter;
import com.example.tuze.bluecollar.adapters.SwipeDeckAdapter;
import com.example.tuze.bluecollar.constants.AppConstants;
import com.example.tuze.bluecollar.constants.FirebaseConstants;
import com.example.tuze.bluecollar.model.Position;
import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.model.User;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //private RecyclerView rvPositions;
    private static final String TAG = "HomeActivity";
    @BindView(R.id.swipeDeck)
    SwipeDeck cardStack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawer;
    @BindView(R.id.navView)
    NavigationView navigationView;
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
       // getSupportActionBar().setIcon(R.mipmap.title);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_homescreen);

        //getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.login_background));


        user=(User) Parcels.unwrap(getIntent().getParcelableExtra(AppConstants.USER));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //rvPositions=(RecyclerView) findViewById(R.id.rvPositions);
        positions = new ArrayList<Position>();
        jobSeekers = new ArrayList<User>();

        if (user.getType() == 1) {
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

                }

            });
            positionsAdapter = new SwipeDeckAdapter(this, positions, user);
            cardStack.setAdapter(positionsAdapter);

        } else {
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

        if (id == R.id.action_settings) {
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
