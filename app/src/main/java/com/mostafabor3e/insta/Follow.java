package com.mostafabor3e.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mostafabor3e.insta.Adapter.AdapterUser;
import com.mostafabor3e.insta.Model.User;

import java.util.ArrayList;
import java.util.List;

public class Follow extends AppCompatActivity {
    String id;
    String title;
    RecyclerView recyclerView;
    ImageView imageView;
    AdapterUser adapterUser;
    List<User>userList;
    List<String>idlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        Intent intent=getIntent();

       id=intent.getStringExtra("id");
       title=intent.getStringExtra("title");
       imageView=findViewById(R.id.Editeprofile_close);
       imageView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
       recyclerView=findViewById(R.id.rec_follow);
       userList=new ArrayList<>();
       idlist=new ArrayList<>();
       recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
       recyclerView.setHasFixedSize(true);

       switch (title){
           case "follower":
               getFollower(id);
               break;
           case "following":
               getFollowing(id);
               break;
           case "likes":
               getLikes(id);
               break;
       }

    }
    private void getFollower(final String id){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("follow").child(this.id).child("followers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idlist.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    idlist.add(dataSnapshot.getKey());
                }
                showUser();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getLikes(final String id){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Likes").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idlist.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    idlist.add(dataSnapshot.getKey());
                }
                showUser();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getFollowing(String id){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("follow").child(this.id).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idlist.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    idlist.add(dataSnapshot.getKey());
                }
                showUser();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showUser(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    for (String id:idlist){
                        if (id.equals(user.getId())){
                            userList.add(user);
                        }
                    }
                }
                adapterUser=new AdapterUser(userList,getBaseContext());
                recyclerView.setAdapter(adapterUser);
                adapterUser.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}