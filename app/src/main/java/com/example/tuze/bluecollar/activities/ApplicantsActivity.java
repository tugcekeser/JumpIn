package com.example.tuze.bluecollar.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.adapters.ApplicantListAdapter;
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

public class ApplicantsActivity extends AppCompatActivity {
    private static final String TAG = "ApplicantsActivity";

    @BindView(R.id.lvApplicants)
    ListView lvApplicantsList;
    ApplicantListAdapter adapter;
    List<User> applicants;
    User user;
    Position position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicants);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Applicants");

        ButterKnife.bind(this);
        Intent intent = getIntent();
        user = (User) Parcels.unwrap(intent.getParcelableExtra(AppConstants.USER));
        position = (Position) Parcels.unwrap(intent.getParcelableExtra("Position"));

        applicants = new ArrayList<User>();

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query queryPosition = ref.child("applications").orderByChild("positionReference").equalTo(position.getPositionReference());
        queryPosition.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Application application = userSnapshot.getValue(Application.class);
                    Query queryUser = ref.child("user").orderByChild("email").equalTo(application.getUserId());
                    queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                applicants.add(user);
                            }
                            adapter = new ApplicantListAdapter(ApplicantsActivity.this, applicants, user);
                            lvApplicantsList.setAdapter(adapter);

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
