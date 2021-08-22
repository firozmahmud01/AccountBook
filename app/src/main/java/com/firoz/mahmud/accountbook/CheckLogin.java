package com.firoz.mahmud.accountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CheckLogin extends AppCompatActivity {
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_login);
        user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            startActivity(new Intent(CheckLogin.this,MainActivity.class));
        }else{
            startActivity(new Intent(CheckLogin.this,LogIn.class));
        }
    }
}
