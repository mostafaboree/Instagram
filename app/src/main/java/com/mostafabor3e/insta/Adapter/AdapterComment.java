package com.mostafabor3e.insta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mostafabor3e.insta.Model.Comment;
import com.mostafabor3e.insta.Model.User;
import com.mostafabor3e.insta.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.CommentHolder> {
    List<Comment> comments;
    Context mcontext;

    public AdapterComment(List<Comment> comments, Context mcontext) {
        this.comments = comments;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.custom_comment_item, null, false);

        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        Comment comment=comments.get(position);
        holder.continte.setText(comment.getComment());
        publisherData(holder.imageUser,holder.username,comment.getPublisher());

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        CircleImageView imageUser;
        TextView username, continte;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            imageUser = itemView.findViewById(R.id.iv_coment_c);
            username = itemView.findViewById(R.id.username_commet_c);
            continte = itemView.findViewById(R.id.tv_get_comment);
        }
    }

    public void publisherData(final CircleImageView imageView, final TextView username, final String postId) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                Glide.with(mcontext).load(user.getImage()).into(imageView);
                username.setText(user.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}