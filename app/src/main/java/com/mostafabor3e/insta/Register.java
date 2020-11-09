package com.mostafabor3e.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
Button button;
EditText username,fullName,email,password;
Task<AuthResult> firebaseAuth;
FirebaseUser firebaseUser;
DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=findViewById(R.id.et_log_email);
        fullName=findViewById(R.id.ed_password_log);
        email=findViewById(R.id.ed_Email_reg);
        password=findViewById(R.id.ed_password_reg);
        button=findViewById(R.id.bt_register_reg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String user_name = username.getText().toString();
                String Gmail = email.getText().toString();
                String fullname = fullName.getText().toString();
                String passw = password.getText().toString();
                if (TextUtils.isEmpty(user_name) ||TextUtils.isEmpty(Gmail)
                        ||TextUtils.isEmpty(passw) ||TextUtils.isEmpty(fullname)) {
                    Toast.makeText(getBaseContext(), "complete your data", Toast.LENGTH_SHORT).show();
                }
                else {
                    Register(user_name, fullname, Gmail, passw);
                }
            }
        });
    }
    public  void Register(final String username, final String fullname, final String Email , String password) {
        firebaseAuth = FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                            final String id=firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("users").child(id);

                            HashMap<String,String> map=new HashMap<>();
                            map.put("username",username);
                            map.put("fullname",fullname);
                            map.put("id",id);
                            map.put("bio","");
                            map.put("image","https://firebasestorage.googleapis.com/v0/b/chat-3a31c.appspot.com/o/index.png?alt=media&token=34cf6cc8-5f96-4b0f-8710-5f9738485c94");
                            reference.setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent intent=new Intent(getBaseContext(),MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });

                        }
                    }
                });



    }

}