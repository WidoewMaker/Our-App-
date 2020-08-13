package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.ReelsViewHolder> {

    List<MediaObject> mMediaObjectList;
    Context mContext;

    public DemoAdapter(List<MediaObject> mediaObjectList, Context context) {
        this.mMediaObjectList = mediaObjectList;
        this.mContext = context;
    }


    public class ReelsViewHolder extends RecyclerView.ViewHolder {
        public TextView noOfLike, noOfComm, noOfView, description, proName;
        public CircleImageView reelsPro_ImgVw;
        public ImageView Reels_PostImgVw, likesImg, commIMg, shareImg;


        public ReelsViewHolder(@NonNull View itemView) {
            super(itemView);

            Reels_PostImgVw = itemView.findViewById(R.id.reels_postImgVw);
            description = itemView.findViewById(R.id.reels_ly_Descrition);


        }

    }

    @NonNull
    @Override
    public ReelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reels_layout, parent, false);
        return new ReelsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReelsViewHolder holder, int position) {
        MediaObject uploadCurrent = mMediaObjectList.get(position);
        holder.description.setText(uploadCurrent.getvName());
        Picasso.get().load(uploadCurrent.getvImgUrL())
                .fit()
                .centerCrop()
                .into(holder.Reels_PostImgVw);


    }

    @Override
    public int getItemCount() {
        return mMediaObjectList.size();
    }


}
