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

import com.example.tuze.bluecollar.constants.AppConstants;
import com.example.tuze.bluecollar.constants.FirebaseConstants;
import com.example.tuze.bluecollar.model.Position;
import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.model.User;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateJobPostActivity extends AppCompatActivity implements View.OnClickListener {

    private Vibrator vib;
    Animation animShake;
    private User user;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.etJobTitle)
    EditText etJobTitle;
    @BindView(R.id.etDeadline)
    EditText etDeadline;
    @BindView(R.id.etLocation)
    EditText etLocation;
    @BindView(R.id.etSalary)
    EditText etSalary;
    @BindView(R.id.etJobDescription)
    EditText etJobDescription;
    @BindView(R.id.fab)
    FloatingActionButton btnSaveJob;
    @BindView(R.id.jobTitleInputLayoutName)
    TextInputLayout jobTitleInputLayoutName;
    @BindView(R.id.deadlineInputLayoutDob)
    TextInputLayout deadlineInputLayoutDob;
    @BindView(R.id.locationInputLayoutName)
    TextInputLayout locationInputLayoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job_post);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user=(User) Parcels.unwrap(getIntent().getParcelableExtra(AppConstants.USER));

        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        btnSaveJob.setOnClickListener(this);
    }

    private void saveJobPost() {

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

        final String key = FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.POSITIONS).push().getKey();
        final Position position = new Position();
        position.setCompanyName(user.getName());
        position.setTitle(etJobTitle.getText().toString());
        position.setDeadline(etDeadline.getText().toString());
        position.setSalary(etSalary.getText().toString());
        position.setDescription(etJobDescription.getText().toString());
        position.setLocation(etLocation.getText().toString());
        position.setImageLink(user.getProfileImage());
        position.setPositionReference(key);

        //Save job position in Firebase
        FirebaseDatabase.getInstance().getReference().child(FirebaseConstants.POSITIONS).child(key).setValue(position);
        startActivity(new Intent(CreateJobPostActivity.this, HomeActivity.class).putExtra(AppConstants.USER, Parcels.wrap(user)));
        finish();

    }

    private boolean checkJobTitle() {

        if (etJobTitle.getText().toString().trim().isEmpty()) {
            jobTitleInputLayoutName.setErrorEnabled(true);
            jobTitleInputLayoutName.setError(getString(R.string.err_msg_name));
            etJobTitle.setError(getString(R.string.job_title_required));
            return false;
        }
        jobTitleInputLayoutName.setErrorEnabled(false);
        return true;
    }

    private boolean checkLocation() {
        if (etLocation.getText().toString().trim().isEmpty()) {

            locationInputLayoutName.setErrorEnabled(true);
            locationInputLayoutName.setError(getString(R.string.err_msg_name));
            etLocation.setError(getString(R.string.location_required));
            return false;
        }
        locationInputLayoutName.setErrorEnabled(false);
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

                deadlineInputLayoutDob.setError(getString(R.string.err_msg_dob));
                etDeadline.setError(getString(R.string.err_msg_required));

                return false;
            }
        } catch (Exception ex) {
            deadlineInputLayoutDob.setError(getString(R.string.err_msg_dob));
            return false;
        }

        etDeadline.setError(null);
        return true;
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
        int id = view.getId();

        if (id == R.id.fab) {
            saveJobPost();
        }
    }
}
