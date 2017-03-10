package com.example.tuze.bluecollar.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tuze.bluecollar.R;
import com.example.tuze.bluecollar.constants.AppConstants;
import com.example.tuze.bluecollar.model.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ZoomImageActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.btnDone)
    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        String photoLink=extras.getString("ImageLink");
        Picasso.with(this).load(photoLink).into(ivImage);
        btnDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.btnDone)
            supportFinishAfterTransition();
    }
}
