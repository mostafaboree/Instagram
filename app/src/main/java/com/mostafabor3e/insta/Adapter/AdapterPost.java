package com.mostafabor3e.insta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mostafabor3e.insta.Comment;
import com.mostafabor3e.insta.Follow;
import com.mostafabor3e.insta.Fragment.PostDetilse;
import com.mostafabor3e.insta.Fragment.Profile;
import com.mostafabor3e.insta.Model.Post;
import com.mostafabor3e.insta.Model.User;
import com.mostafabor3e.insta.Poast;
import com.mostafabor3e.insta.R;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.PostHolder> {
    Context mcontext;
    List<Post>postList;
    LesinerClick lesinerClick;
    ImagePostClick imagePostClick;
    UserClick userClick;

    public AdapterPost(Context mcontext, List<Post> postList, LesinerClick lesinerClick, UserClick userClick,ImagePostClick imagePostClick) {
        this.mcontext = mcontext;
        this.postList = postList;
        this.lesinerClick = lesinerClick;
        this.userClick = userClick;
        this.imagePostClick=imagePostClick;
    }

    public interface LesinerClick{
        void click(View view,int potion);
    }
    public interface UserClick{
         void User(View view, int potion);
    }
    public interface ImagePostClick{
        void User(View view, int potion);
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.custom_item_post,null,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostHolder holder, final int position) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post=postList.get(position);
       // Uri uri=Uri.parse(post.getPoasImage());
        Glide.with(mcontext).load(post.getPostimage()).into(holder.imagePost);
        //Toast.makeText(mcontext, "uri "+post.getPoasImage(), Toast.LENGTH_SHORT).show();
        if(post.getDescraption().equals("")){
            holder.description.setVisibility(View.GONE);
        }else {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(post.getDescraption().toString());

        }
        publisherData(holder.userImagePost,holder.username,holder.publisher,post.getPublisher());
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference("Likes")
                            .child(post.getPostId())
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("true");
                    addNotification(post.getPostId(),post.getPublisher());
                }
                else {
                    FirebaseDatabase.getInstance().getReference("Likes")
                            .child(post.getPostId())
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                }
            }
        });
        Liked(holder.like,post.getPostId());
        nlikes(holder.likes,post.getPostId());
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               lesinerClick.click(v,position);
            }
        });
        holder.userImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userClick.User(v,position);

            }
        });
        holder.imagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagePostClick.User(v,position);

            }
        });
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.save.getTag().equals("save")){
                   // holder.save.setImageResource(R.drawable.ic_save);
                    FirebaseDatabase.getInstance().getReference("save").child(firebaseUser.getUid())
                            .child(post.getPostId()).setValue(true);
                }
                else {
                   // holder.save.setTag("saved");
                    FirebaseDatabase.getInstance().getReference("save").child(firebaseUser.getUid())
                            .child(post.getPostId()).removeValue();

                }
            }
        });

        nComments(post.getPostId(),holder.comments);
        Saved(post.getPostId(),holder.save);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        ImageView imagePost,like,comment,save;
        CircleImageView userImagePost;
        TextView likes,comments,publisher,description,username;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            imagePost=itemView.findViewById(R.id.iv_post_p);
            like=itemView.findViewById(R.id.iv_like_p);
            comment=itemView.findViewById(R.id.iv_comment_post);
            save=itemView.findViewById(R.id.iv_save_p);
            userImagePost=itemView.findViewById(R.id.iv_user_cp);
            likes=itemView.findViewById(R.id.tv_likes_p);
            comments=itemView.findViewById(R.id.tv_viewAllcomen_p);
            publisher=itemView.findViewById(R.id.tv_publisher_p);
            description=itemView.findViewById(R.id.tv_des_p);
            username=itemView.findViewById(R.id.tv_usename_p);

        }
    }
    public void publisherData(final CircleImageView imageView, final TextView username, final TextView publisher, String userId){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User user=snapshot.getValue(User.class);
                    Glide.with(mcontext).load(user.getImage()).into(imageView);
                    username.setText(user.getUsername());
                    publisher.setText(user.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void Liked(final ImageView imageView, String postId){
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setTag("liked");
                    imageView.setImageResource(R.drawable.liked);
                }
                else {
                    imageView.setImageResource(R.drawable.ic_fave);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void nlikes(final TextView textView, String id){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Likes").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText(snapshot.getChildrenCount()+" likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void nComments(String postId, final TextView textView){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Comment").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText(snapshot.getChildrenCount()+" View All comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void Saved(final String id, final ImageView imageView){
       final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("save").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists()){

                    imageView.setTag("saved");
                    imageView.setImageResource(R.drawable.ic_save);

                }
                else {

                    imageView.setTag("save");
                    imageView.setImageResource(R.drawable.ic_saved);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void addNotification(String postId,String userId){
        FirebaseUser  firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("notifications").child(userId);
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("userId",firebaseUser.getUid());
        hashMap.put("postId",postId);
        hashMap.put("text","like your post");
        hashMap.put("ispost",true);
        reference.push().setValue(hashMap);



    }
}
