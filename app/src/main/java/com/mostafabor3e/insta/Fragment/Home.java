package com.mostafabor3e.insta.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mostafabor3e.insta.Adapter.AdapterPost;
import com.mostafabor3e.insta.Adapter.AdapterStory;
import com.mostafabor3e.insta.Comment;
import com.mostafabor3e.insta.Model.Post;
import com.mostafabor3e.insta.Model.Story;
import com.mostafabor3e.insta.Poast;
import com.mostafabor3e.insta.R;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {
    RecyclerView recyclerView;
    RecyclerView recyclerViewS;
    AdapterStory adapterStory;
    List<Story>stories;
    List<Post>postList;
    AdapterPost adapterPost;
    List<String>following;


    public Home() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.rec_home_post);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        postList=new ArrayList<>();
        adapterPost=new AdapterPost(getContext(), postList, new AdapterPost.LesinerClick() {
            @Override
            public void click(View view, int potion) {
                Intent intent = new Intent(getContext(), Comment.class);
                intent.putExtra("postId", postList.get(potion).getPostId());
                intent.putExtra("userId", postList.get(potion).getPublisher());
                startActivity(intent);
            }
        }, new AdapterPost.UserClick() {
            @Override
            public void User(View view, int potion) {
                // Intent intent = new Intent(getContext(), Profile.class);
                SharedPreferences.Editor sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                sharedPreferences.putString("user_id", postList.get(potion).getPublisher());
                sharedPreferences.apply();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragmet_continer, new Profile()).commit();

                // startActivity(intent);

            }
        }, new AdapterPost.ImagePostClick() {
            @Override
            public void User(View view, int potion) {
                SharedPreferences.Editor sharedPreferences = getContext().getSharedPreferences("PREF", Context.MODE_PRIVATE).edit();
                sharedPreferences.putString("post_id",postList.get(potion).getPostId());
                sharedPreferences.apply();
                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragmet_continer, new PostDetilse()).commit();

            }
        });

        recyclerViewS=view.findViewById(R.id.rec_stories_hom);
        recyclerViewS.setHasFixedSize(true);
        recyclerViewS.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        stories=new ArrayList<>();
        adapterStory=new AdapterStory(stories,getContext());
        recyclerViewS.setAdapter(adapterStory);


        following=new ArrayList<>();
        recyclerView.setAdapter(adapterPost);
        cheackfollow();
        return view;
    }
    private void cheackfollow(){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("follow").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following.clear();
                for (DataSnapshot snap:snapshot.getChildren()){
                    following.add(snap.getKey());
                }
                following.add(FirebaseAuth.getInstance().getCurrentUser().getUid());

                readPost();
                readStory();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readPost(){
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot snap:snapshot.getChildren()) {
                    Post post = snap.getValue(Post.class);
                    for (String id : following) {
                        if (id.equals(post.getPublisher())) {
                            postList.add(post);
                        }
                    }

                }
                adapterPost.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readStory(){
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long timecurrent=System.currentTimeMillis();
                stories.clear();
                stories.add(new Story("",firebaseUser.getUid(),"",0,0));
                    for (String id:following){
                        int counstory=0;
                        Story story=null;
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            story=snapshot.getValue(Story.class);
                            if (timecurrent>story.getStarttime()&&timecurrent<story.getEndtime()){
                                counstory++;
                            }
                        }
                        if (counstory>0){
                            stories.add(story);
                        }
                    }

                adapterStory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}