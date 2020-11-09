package com.mostafabor3e.insta.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.internal.$Gson$Preconditions;
import com.mostafabor3e.insta.Model.Post;
import com.mostafabor3e.insta.R;

import java.util.List;

public class AdapterPhotoes extends RecyclerView.Adapter<AdapterPhotoes.PhotoHolder> {
    List<Post>posts;
    Context context;
    Dialog  dialog;

    public AdapterPhotoes(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }


    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_photoes,null,false);
        dialog=new Dialog(context);
       // dialog.setContentView(R.layout.custom_photoes);

        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoHolder holder, final int position) {
       final Post post=posts.get(position);
        Glide.with(context).load(post.getPostimage()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imageView;
                imageView=dialog.findViewById(R.id.iv_custom_photoes);
            //    imageView.setImageResource(holder.imageView.getImageAlpha());

            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.iv_custom_photoes);
        }
    }
}
