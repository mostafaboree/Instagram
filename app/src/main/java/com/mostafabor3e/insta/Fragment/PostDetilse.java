package com.mostafabor3e.insta.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mostafabor3e.insta.Adapter.AdapterPost;
import com.mostafabor3e.insta.Model.Post;
import com.mostafabor3e.insta.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PostDetilse extends Fragment {

    RecyclerView recyclerView;
    AdapterPost adapterPost;
    String postID;
    List<Post> postList;

    public PostDetilse() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_detilse, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences("PREF", Context.MODE_PRIVATE);
        postID = preferences.getString("post_id", "none");
        postList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rec_detilse);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterPost = new AdapterPost(getContext(), postList, new AdapterPost.LesinerClick() {
            @Override
            public void click(View view, int potion) {

            }
        }, new AdapterPost.UserClick() {
            @Override
            public void User(View view, int potion) {

            }
        }, new AdapterPost.ImagePostClick() {
            @Override
            public void User(View view, int potion) {

            }
        });
        recyclerView.setAdapter(adapterPost);
        ReSavePost();
        return view;
    }

    private void ReSavePost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    if (postID.equals(post.getPostId())){
                        postList.add(post);
                        Toast.makeText(getContext(), ""+post.getDescraption(), Toast.LENGTH_SHORT).show();

                    }

                }


                // Toast.makeText(getContext(), "is"+post.getDescraption(), Toast.LENGTH_SHORT).show()

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}