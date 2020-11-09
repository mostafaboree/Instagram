package com.mostafabor3e.insta.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mostafabor3e.insta.Adapter.AdapterPhotoes;
import com.mostafabor3e.insta.EditeProfile;
import com.mostafabor3e.insta.Follow;
import com.mostafabor3e.insta.Model.Post;
import com.mostafabor3e.insta.Model.User;
import com.mostafabor3e.insta.Poast;
import com.mostafabor3e.insta.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {
ImageView save,photos,menu;
CircleImageView imageUser;
TextView posts,followers,following,username,fullname,bio;
List<Post>postList;
    List<Post>savePost;
    List<String>idPost;
Button follow;
String profileID;
FirebaseUser firebaseUser;
AdapterPhotoes adapterPost;
AdapterPhotoes photoes;
RecyclerView recyclerViewPh,recyclerViewSavePost;
    public Profile() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view=inflater.inflate(R.layout.fragment_profile, container, false);
         recyclerViewPh=view.findViewById(R.id.rec_profile_f);
      //   RecyclerView.LayoutManager  linearLayoutManager=new GridLayoutManager(getContext(),3)
        SharedPreferences  preferences=getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileID=preferences.getString("user_id","none");
       // Toast.makeText(getContext(), "id"+preferences, Toast.LENGTH_SHORT).show();
        save=view.findViewById(R.id.iv_save_profile);
        photos=view.findViewById(R.id.iv_photots);
        menu=view.findViewById(R.id.iv_proflie_option);
        imageUser=view.findViewById(R.id.iv_profile_user);
        posts=view.findViewById(R.id.tv_n_post);
        followers=view.findViewById(R.id.tv_n_followers);
        following=view.findViewById(R.id.tv_n_following);
        username=view.findViewById(R.id.tv_username_profile);
        follow=view.findViewById(R.id.bt_profile_follow);
        fullname=view.findViewById(R.id.tv_profile_publisher);
        bio=view.findViewById(R.id.tv_bio);
        recyclerViewPh.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerViewPh.setHasFixedSize(true);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        postList=new ArrayList<>();
        photoes=new AdapterPhotoes(postList,getContext());
      recyclerViewPh.setAdapter(photoes);
       // recyclerViewSavePost.setAdapter(photoes);

        ///rec save post
        savePost=new ArrayList<>();
        idPost=new ArrayList<>();
        recyclerViewSavePost=view.findViewById(R.id.rec_profile_post);
        recyclerViewSavePost.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerViewSavePost.setHasFixedSize(true);
       // recyclerViewPh.setVisibility(View.GONE);
        adapterPost=new AdapterPhotoes(savePost,getContext());
        recyclerViewSavePost.setAdapter(adapterPost);
        recyclerViewPh.setVisibility(View.VISIBLE);
        recyclerViewSavePost.setVisibility(View.GONE);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        CheackSaved();
        ReSavePost();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewPh.setVisibility(View.GONE);
                recyclerViewSavePost.setVisibility(View.VISIBLE);
            }
        });
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewPh.setVisibility(View.VISIBLE);
                recyclerViewSavePost.setVisibility(View.GONE);
            }
        });
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Follow.class);
                intent.putExtra("id",profileID);
                intent.putExtra("title","follower");
                startActivity(intent);

            }
        });
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Follow.class);
                intent.putExtra("id",profileID);
                intent.putExtra("title","following");
                startActivity(intent);

            }
        });


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow.getText().equals("Edit Profile")){
                    //go edit
                    Intent intent=new Intent(getContext(), EditeProfile.class);
                    startActivity(intent);
                   // Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
                }
                else if (follow.getText().equals("follow")){
                        FirebaseDatabase.getInstance().getReference().child("follow").
                                child(firebaseUser.getUid()).child("following").child(profileID).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("follow").
                                child(profileID).child("followers").child(firebaseUser.getUid()).setValue(true);
                    }
                    else if (follow.getText().equals("following")) {
                        FirebaseDatabase.getInstance().getReference().child("follow").
                                child(firebaseUser.getUid()).child("following").child(profileID).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("follow").
                                child(profileID).child("followers").child(firebaseUser.getUid()).removeValue();
                    }

            }
        });

  dataPublisher();
  getPhotoes();
   getFollower();
   Npost();
    getFollowing();
   if (profileID.equals(firebaseUser.getUid())){
    follow.setText("Edit Profile");
}else {
    following();
    save.setVisibility(View.GONE);

}





        return view;
    }
    private void dataPublisher(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(profileID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext()==null){
                    return;
                }
                User user=snapshot.getValue(User.class);
                //circleImageView.setImageResource(user.getImage());
                assert user != null;
                Glide.with(getContext()).load(user.getImage()).into(imageUser);
                username.setText(user.getUsername().toString());
                bio.setText(user.getBio().toString());
                fullname.setText(user.getFullname().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void following(){

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(profileID).exists()){
                    follow.setText("following");
                }
                else {
                    follow.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getFollower(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("follow").
                child(profileID).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.setText(snapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void getFollowing(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("follow").
                child(profileID).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following.setText(snapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void Npost(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i=0;
                for (DataSnapshot snap:snapshot.getChildren()){
                    Post post=snap.getValue(Post.class);
                    if (post.getPublisher().equals(profileID)){
                        i++;
                    }
                    posts.setText(i+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private  void getPhotoes(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Post post=dataSnapshot.getValue(Post.class);
                    if(post.getPublisher().equals(profileID)){
                       // Toast.makeText(getContext(), "uri"+post.getPostimage(), Toast.LENGTH_SHORT).show();
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                photoes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error in getPhoto", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void ReSavePost(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                savePost.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Post post=dataSnapshot.getValue(Post.class);
                    for (String id:idPost){
                        if (id.equals(post.getPostId())){
                            savePost.add(post);
                           // Toast.makeText(getContext(), "is"+post.getDescraption(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                Collections.reverse(savePost);
                adapterPost.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void CheackSaved(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("save").child(profileID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idPost.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    idPost.add(dataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}