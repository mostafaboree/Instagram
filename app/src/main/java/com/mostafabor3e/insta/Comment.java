package com.mostafabor3e.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mostafabor3e.insta.Adapter.AdapterComment;
import com.mostafabor3e.insta.Fragment.Home;
import com.mostafabor3e.insta.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comment extends AppCompatActivity {
    EditText writeComment;
    ImageView close;
    Button add;
    CircleImageView imageUser;
    RecyclerView recyclerView;
    FirebaseUser firebaseUser;
    List<com.mostafabor3e.insta.Model.Comment>comments;
    AdapterComment adapterComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        recyclerView=findViewById(R.id.rec_comment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setHasFixedSize(true);
        comments= new ArrayList<>();
       writeComment=findViewById(R.id.ed_comment_comment);
        add=findViewById(R.id.bt_add_comment);
        close=findViewById(R.id.Editeprofile_close);
        imageUser=findViewById(R.id.iv_user_comment);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Intent  intent=getIntent();
      final String id= intent.getStringExtra("postId");
      final String publisher=intent.getStringExtra("userId");
      retriveComment(id);
      add.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (writeComment.getText().toString().equals("")){
                  Toast.makeText(Comment.this, "can not add empty comment", Toast.LENGTH_SHORT).show();
              }
              else {
                  addComment(id);
                  addNotification(id,publisher);

              }
          }
      });
      dataPublisher(publisher,imageUser);

      close.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent=new Intent(getBaseContext(),Home.class);
              startActivity(intent);
          }
      });

    }
    private void addComment(String postId){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Comment").child(postId);
        String id=reference.getKey();
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("comment",writeComment.getText().toString());
        hashMap.put("id",id);
        hashMap.put("publisher",firebaseUser.getUid());
        reference.push().setValue(hashMap);

        writeComment.setText("");

    }
    private void dataPublisher(String id, final CircleImageView circleImageView){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                //circleImageView.setImageResource(user.getImage());
                assert user != null;
                Glide.with(getBaseContext()).load(user.getImage()).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void retriveComment(String postID){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Comment").child(postID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.clear();
                for (DataSnapshot snap:snapshot.getChildren()){
                    com.mostafabor3e.insta.Model.Comment comment=snap.getValue(com.mostafabor3e.insta.Model.Comment.class);
                    comments.add(comment);
                   // Toast.makeText(Comment.this, "c"+comment.getComment() ,Toast.LENGTH_SHORT).show();
                }
                adapterComment=new AdapterComment(comments,getBaseContext());
                recyclerView.setAdapter(adapterComment);
                //adapterComment.notifyDataSetChanged();
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
        hashMap.put("text","add comment");
        hashMap.put("ispost",true);
        reference.push().setValue(hashMap);



    }
}