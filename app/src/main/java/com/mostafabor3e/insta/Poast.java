package com.mostafabor3e.insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Poast extends AppCompatActivity {
Uri imageUri;
EditText descraption;
TextView post;
ImageView imageView;
public static final int image_return=1;
ImageView close;
FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poast);
        descraption=findViewById(R.id.ed_post);
        post=findViewById(R.id.Editeprofile_save);
        imageView=findViewById(R.id.iv_post);
        close=findViewById(R.id.Editeprofile_close);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),MainActivity.class));
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //createPost(descraption.getText().toString(),imageUri);
                uploadimage();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImage();

            }
        });
       // imageView.setImageURI(imageUri);
        // */CropImage.activity().setAspectRatio(1,1).
               // start(Poast.this);

    }
    public void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,image_return);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==image_return&&resultCode==RESULT_OK){

            imageUri=data.getData();
           imageView.setImageURI(imageUri);
        }
    }

private void uploadimage(){
    final StorageReference reference= FirebaseStorage.getInstance().getReference().child("postImage");
    final StorageReference storageReference=reference.child(imageUri.getLastPathSegment());

    storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            storageReference.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String dawnload=String.valueOf(uri);
                    databaseReference=FirebaseDatabase.getInstance().getReference("posts").push();
                    String postId=databaseReference.getKey();
                    assert postId != null;
                    HashMap<String,Object>hashMap=new HashMap<>();
                    hashMap.put("descraption",descraption.getText().toString());
                    hashMap.put("publisher",firebaseUser.getUid());
                    hashMap.put("postimage",dawnload);
                    hashMap.put("postId",postId);
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(getBaseContext(),MainActivity.class));
                            }
                        }
                    });

                    }

            });
        }
    });
}
private void createPost(String des,Uri image){
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("posts").child(firebaseUser.getUid());
    String postId=databaseReference.push().getKey();
    assert postId != null;
    databaseReference.child(postId);
    HashMap<String,Object>hashMap=new HashMap<>();
    hashMap.put("descrption",des);
    hashMap.put("user_id",firebaseUser.getUid());
    hashMap.put("imagepost",image);
    hashMap.put("postId",postId);
    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()){
                startActivity(new Intent(getBaseContext(),MainActivity.class));
            }
        }
    });
}
}