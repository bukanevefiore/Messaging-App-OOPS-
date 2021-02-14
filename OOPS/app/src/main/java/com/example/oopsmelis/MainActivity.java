package com.example.oopsmelis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // henüz kullanıcı girişi olmamışsa uygulma kayıt ol ekranına yönlendirme
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tanimlamalar(){
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }

    // user kontrol
    public void kontrol(){

    }
}