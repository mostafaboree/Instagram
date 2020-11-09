package com.mostafabor3e.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
Button login,ok,close;
EditText Email,password;
FirebaseUser firebaseUser;
FirebaseAuth firebaseAuth;
Dialog dialog;
ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        login=findViewById(R.id.bt_login_log);
        Email=findViewById(R.id.et_log_email);
        password=findViewById(R.id.ed_password_log);
        firebaseAuth=FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vPassword=password.getText().toString();
                String vEmail=Email.getText().toString();
                if (TextUtils.isEmpty(vEmail)||TextUtils.isEmpty(vPassword)){
                    creatDilog();
                    Toast.makeText(Login.this, "Complete your data", Toast.LENGTH_SHORT).show();
                }
                else {
                    singin(vEmail, vPassword);
                }

            }
        });
    }
    public void creatDilog(){
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.custom_dilog);
        ok=dialog.findViewById(R.id.bt_dilge_register);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        // popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,500);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    }
    public void singin(String Email,String password){
        firebaseAuth.signInWithEmailAndPassword( Email,password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent=new Intent(getBaseContext(),MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else {
                           creatDilog();
                        }
                    }
                });
    }
}