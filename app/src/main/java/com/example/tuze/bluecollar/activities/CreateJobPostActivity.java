package com.example.tuze.bluecollar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.example.tuze.bluecollar.model.Position;
import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.model.User;
import com.google.firebase.database.FirebaseDatabase;

public class CreateJobPostActivity extends AppCompatActivity {

    private Vibrator vib;
    Animation animShake;
    private EditText etJobTitle, etDeadline, etLocation, etSalary,etJobDescription;
    private TextInputLayout job_title_input_layout_name, deadline_input_layout_dob,
            location_input_layout_name;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        user = (User) intent.getSerializableExtra("User");

        etDeadline=(EditText) findViewById(R.id.etDeadline);
        etJobTitle=(EditText)findViewById(R.id.etJobTitle);
        etLocation=(EditText) findViewById(R.id.etLocation);
        etSalary=(EditText)findViewById(R.id.etSalary);
        etJobDescription=(EditText) findViewById(R.id.etJobDescription);

        job_title_input_layout_name=(TextInputLayout)findViewById(R.id.job_title_input_layout_name);
        deadline_input_layout_dob=(TextInputLayout)findViewById(R.id.deadline_input_layout_dob);
        location_input_layout_name=(TextInputLayout)findViewById(R.id.location_input_layout_name);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              saveJobPost();
            }
        });
    }

    private void saveJobPost(){

        if (!checkJobTitle()) {
            etJobTitle.setAnimation(animShake);
            etJobTitle.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkDOB()) {
            etDeadline.setAnimation(animShake);
            etDeadline.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkLocation()) {
            etLocation.setAnimation(animShake);
            etLocation.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }

        final String key = FirebaseDatabase.getInstance().getReference().child("positions").push().getKey();
        final Position position=new Position();
        position.setCompanyName(user.getName());
        position.setTitle(etJobTitle.getText().toString());
        position.setDeadline(etDeadline.getText().toString());
        position.setSalary(etSalary.getText().toString());
        position.setDescription(etJobDescription.getText().toString());
        position.setLocation(etLocation.getText().toString());
        position.setImageLink(user.getProfileImage());
        position.setPositionReference(key);

        FirebaseDatabase.getInstance().getReference().child("positions").child(key).setValue(position);

        /*DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("users").orderByChild("name").equalTo("Ephesus Restaurant");

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    // appleSnapshot.getRef().ge;
                    //User person = appleSnapshot.getValue(User.class);
                    appleSnapshot.getRef().child("positions").child(key).setValue(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/

        startActivity(new Intent(CreateJobPostActivity.this, HomeActivity.class).putExtra("User",user));
        finish();

    }

    private boolean checkJobTitle() {
        if (etJobTitle.getText().toString().trim().isEmpty()) {

            job_title_input_layout_name.setErrorEnabled(true);
            job_title_input_layout_name.setError(getString(R.string.err_msg_name));
            etJobTitle.setError("Job title required");
            return false;
        }
        job_title_input_layout_name.setErrorEnabled(false);
        return true;
    }

    private boolean checkLocation() {
        if (etLocation.getText().toString().trim().isEmpty()) {

            location_input_layout_name.setErrorEnabled(true);
            location_input_layout_name.setError(getString(R.string.err_msg_name));
            etLocation.setError("Location required");
            return false;
        }
        location_input_layout_name.setErrorEnabled(false);
        return true;
    }

    private boolean checkDOB() {

        try {
            boolean isDateValid = false;
            String[] s = etDeadline.getText().toString().split("/");
            int date = Integer.parseInt(s[1]);
            int month = Integer.parseInt(s[0]);

            if (date < 32 && month < 13)
                isDateValid = true;

            if (etDeadline.getText().toString().trim().isEmpty() && isDateValid) {

                deadline_input_layout_dob.setError(getString(R.string.err_msg_dob));
                etDeadline.setError(getString(R.string.err_msg_required));

                return false;
            }
        }catch(Exception ex){
            deadline_input_layout_dob.setError(getString(R.string.err_msg_dob));
            return false;
        }

        etDeadline.setError(null);
        return true;
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
