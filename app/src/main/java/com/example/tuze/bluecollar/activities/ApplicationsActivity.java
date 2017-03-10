package com.example.tuze.bluecollar.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.adapters.ApplicationsAdapter;
import com.example.tuze.bluecollar.constants.AppConstants;
import com.example.tuze.bluecollar.model.Application;
import com.example.tuze.bluecollar.model.Position;
import com.example.tuze.bluecollar.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplicationsActivity extends AppCompatActivity {
    private static final String TAG = "ApplicationsActivity";

    @BindView(R.id.lvApplications)
    ListView lvApplicationsList;
    ApplicationsAdapter adapter;
    List<Position> applications;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applications);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        user = (User) Parcels.unwrap(intent.getParcelableExtra(AppConstants.USER));
        getSupportActionBar().setTitle(user.getName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Applications");

        applications = new ArrayList<Position>();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("applications").orderByChild("userId").equalTo(user.getEmail());

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
                                applications.add(position);
                            }
                            adapter = new ApplicationsAdapter(ApplicationsActivity.this, applications,user);
                            lvApplicationsList.setAdapter(adapter);

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

