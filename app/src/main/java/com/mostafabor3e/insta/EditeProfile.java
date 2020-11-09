package com.mostafabor3e.insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mostafabor3e.insta.Model.User;
import com.mostafabor3e.insta.R;
import com.mostafabor3e.insta.R.*;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditeProfile extends AppCompatActivity {
    EditText fullname,username,bio;
    CircleImageView image;
    ImageView close;
    TextView save,change;
    Uri imageUri;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_profile);
        fullname=findViewById(id.Editeprofile_fullname);
        username=findViewById(id.Editeprofile_username);
        bio=findViewById(id.Editeprofile_bio);
        image=findViewById(id.Editeprofile_image);
        change=findViewById(id.Editeprofile_change);
        close=findViewById(id.Editeprofile_close);
        save=findViewById(id.Editeprofile_save);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                fullname.setText(user.getFullname().toString());
                username.setText(user.getUsername().toString());
                bio.setText(user.getBio().toString());
                Glide.with(getBaseContext()).load(user.getImage()).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeData(fullname.getText().toString(),username.getText().toString(),bio.getText().toString());
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changeData(String fullname, String username,String bio) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        HashMap<String,Object>hashMap=new HashMap<>();
        hashMap.put("fullname",fullname);
        hashMap.put("username",username);
        hashMap.put("bio",bio);
        reference.updateChildren(hashMap);

    }
    private void openImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==3&&resultCode==RESULT_OK&&data !=null){
            imageUri=data.getData();
            image.setImageURI(imageUri);
        }
    }
    private void uploadImage(){
       final StorageReference reference= FirebaseStorage.getInstance().getReference("userPhoto").child(firebaseUser.getUid());

        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditeProfile.this, "success", Toast.LENGTH_SHORT).show();
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                        HashMap<String,Object>hashMap=new HashMap<>();
                        hashMap.put("image",uri.toString());
                        reference.updateChildren(hashMap);

                    }
                });

            }
        });
    }
}