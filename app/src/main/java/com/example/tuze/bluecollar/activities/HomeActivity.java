package com.example.tuze.bluecollar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.tuze.bluecollar.adapters.ApplicantListAdapter;
import com.example.tuze.bluecollar.adapters.PositionsAdapter;
import com.example.tuze.bluecollar.model.Position;
import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView rvPositions;
    private PositionsAdapter positionsAdapter;
    private ApplicantListAdapter jobSeekerAdapter;
    private ArrayList<Position> positions;
    private ArrayList<User> jobSeekers;
    //private ImageView ivIcon;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");

        Intent intent=getIntent();
        user = (User) intent.getSerializableExtra("User");


       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //ivIcon=(ImageView)navigationView.findViewById(R.id.ivIcon);

        //Picasso.with(this).load(user.getProfileImage()).placeholder(R.drawable.avatar_user).to


        rvPositions=(RecyclerView) findViewById(R.id.rvPositions);
        positions=new ArrayList<Position>();
        jobSeekers=new ArrayList<User>();

        if(user.getType()==1) {
            DatabaseReference mListItemRef = FirebaseDatabase.getInstance().getReference().child("positions");

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
            positionsAdapter = new PositionsAdapter(this, positions, user);
            rvPositions.setAdapter(positionsAdapter);
        }
        else{
            DatabaseReference mListItemRef = FirebaseDatabase.getInstance().getReference().child("user");

            mListItemRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        User seeker = postSnapshot.getValue(User.class);
                        if(seeker.getType()==1)
                          jobSeekers.add(seeker);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            jobSeekerAdapter = new ApplicantListAdapter(this, jobSeekers, user);
            rvPositions.setAdapter(jobSeekerAdapter);
        }
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);
        rvPositions.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search...");
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {

                if(user.getType()==1) {
                    ArrayList<Position> searchPositions=new ArrayList<Position>();
                    for(int i=0;i<positions.size();i++) {
                        if(positions.get(i).getTitle().toLowerCase().indexOf(query.toLowerCase())>0) {
                            searchPositions.add(positions.get(i));
                        }
                    }
                    positions.clear();
                    positions=searchPositions;
                    positionsAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

        if (id == R.id.nav_camera) {
            intent = new Intent(this,HomeActivity.class).putExtra("User",user);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {
            intent = new Intent(this,ProfileActivity.class).putExtra("User",user);
            intent.putExtra("ScreenType", "Profile");
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {
            intent = new Intent(this,CreateJobPostActivity.class).putExtra("User",user);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
