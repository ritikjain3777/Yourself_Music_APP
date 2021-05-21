package com.example.yourselfmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            if (firebaseAuth.getCurrentUser().getEmail().equals("sankalpjangid667@gmail.com")){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }else{
                startActivity(new Intent(getApplicationContext(),UserMainView.class));
            }

        }else{
            startActivity(new Intent(getApplicationContext(),UserMainView.class));
        }

    }
}
