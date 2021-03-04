package com.example.oopsmelis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckedTextView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.oopsmelis.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class SignInActivity extends AppCompatActivity {

    TextInputEditText emailSignInEditText,passwordSignInEditText;
    Button signInButon;
    FirebaseAuth auth;
    AppCompatCheckedTextView signUpGecis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        tanimlamalar();
    }

    public void tanimlamalar(){
        emailSignInEditText=findViewById(R.id.emailSignInEditText);
        passwordSignInEditText=findViewById(R.id.passwordSignInEditText);
        signInButon=findViewById(R.id.signInButon);
        signUpGecis=findViewById(R.id.signUpGecis);
        auth=FirebaseAuth.getInstance();




        signUpGecis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });


        signInButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailSignInEditText.getText().toString();
                String pass=passwordSignInEditText.getText().toString();

                if(!email.equals("") && !pass.equals("")){

                    login(email,pass);
                    Log.i("hata1", "" + auth);
                    Log.i("hata2", "" + email);
                    Log.i("hata3", "" + pass);

                }
                else{
                    Toast.makeText(getApplicationContext(), "Fill in all fields!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // sisteme giriş
    public void login(String email,String pass) {

try {
    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {


            if (task.isSuccessful()) {

                Log.i("hataaamı","hata");
                Intent intent = new Intent(SignInActivity.this, ButtomNavigationActivity.class);
                startActivity(intent);
                finish();


            } else {

                Toast.makeText(getApplicationContext(), "There was a problem!!", Toast.LENGTH_LONG).show();

            }
        }
    });

}catch (Exception e){
    Log.i("hatasignın", e.getMessage());
}

}
}