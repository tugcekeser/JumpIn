package com.example.tuze.bluecollar.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tuze.bluecollar.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotosCardAdapter extends RecyclerView.Adapter<PhotosCardAdapter.PhotosViewHolder> {

    ArrayList<String> photos;
    Context mContext;

    public PhotosCardAdapter(Context context, ArrayList<String> p) {
        mContext = context;
        photos = p;
    }

    @Override
    public PhotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        PhotosCardAdapter.PhotosViewHolder vh = new PhotosCardAdapter.PhotosViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(PhotosViewHolder holder, int position) {

        String photoLink = photos.get(position);
        Picasso.with(mContext).load(photoLink).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PhotosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivImage;
        public View view;


        public PhotosViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            ivImage.setImageResource(0);
           /*
            icon.setOnClickListener(this);*/
        }

        @Override
        public void onClick(View view) {

        }
    }

}
