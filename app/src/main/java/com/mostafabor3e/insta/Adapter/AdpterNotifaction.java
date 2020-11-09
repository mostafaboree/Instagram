package com.mostafabor3e.insta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mostafabor3e.insta.Model.Notifaction;
import com.mostafabor3e.insta.Model.Post;
import com.mostafabor3e.insta.Model.User;
import com.mostafabor3e.insta.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdpterNotifaction extends RecyclerView.Adapter<AdpterNotifaction.NotifiHolder> {
    List<Notifaction>notifactionList;
    Context  mcontext;

    public AdpterNotifaction(List<Notifaction> notifactionList, Context mcontext) {
        this.notifactionList = notifactionList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public NotifiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.notfication_item,null,false);
        return new NotifiHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiHolder holder, int position) {
        Notifaction  notifaction=notifactionList.get(position);
        holder.desc.setText(notifaction.getText().toString());
        publisherData(holder.imageUser,holder.username,notifaction.getUserId());
        if (notifaction.isIspost()){
            holder.imagePost.setVisibility(View.VISIBLE);
            getImage(holder.imagePost,notifaction.getPostId());
        }
        else {
            holder.imagePost.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        return notifactionList.size();
    }

    public class NotifiHolder extends RecyclerView.ViewHolder {
        CircleImageView imageUser;
        ImageView imagePost;
        TextView username,desc;
        public NotifiHolder(@NonNull View itemView) {
            super(itemView);
            imagePost=itemView.findViewById(R.id.notfication_iv_post);
            imageUser=itemView.findViewById(R.id.notfication_iv_user);
            username=itemView.findViewById(R.id.notfication_username);
            desc=itemView.findViewById(R.id.notfication_des);



        }
    }
    private void getImage(final ImageView circleImageView,  String publisherid){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("posts").child(publisherid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post user=snapshot.getValue(Post.class);

                Glide.with(mcontext).load(user.getPostimage()).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void publisherData(final CircleImageView  circleImageView, final TextView username, String publisherid){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(publisherid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                username.setText(user.getUsername().toString());
                Glide.with(mcontext).load(user.getImage()).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
