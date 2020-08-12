package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.DemoViewHolderr> {

    List<MediaObject> mediaObjectList;
    Context context;

    public DemoAdapter(List<MediaObject> mediaObjectList, Context context) {
        this.mediaObjectList = mediaObjectList;
        this.context = context;
    }


    public class ReelsViewHolder extends RecyclerView.ViewHolder {
        public TextView noOfLike, noOfComm, noOfView, description, proName;
        public CircleImageView reelsPro_ImgVw;
        public ImageView Reels_PostImgVw, likesImg, commIMg, shareImg;


        public ReelsViewHolder(@NonNull View itemView) {
            super(itemView);

            likesImg = itemView.findViewById(R.id.reelLikeImgVw);


        }

    }

    @NonNull
    @Override
    public DemoViewHolderr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reels_layout, parent, false);
        return new DemoViewHolderr(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DemoViewHolderr holder, int position)
    {


    }

    @Override
    public int getItemCount() {
        return mediaObjectList.size();
    }

    public class DemoViewHolderr extends RecyclerView.ViewHolder {
        public DemoViewHolderr(@NonNull View itemView) {
            super(itemView);
        }
    }
}
