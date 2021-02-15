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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText emailEditText,passwordEditText;
    Button signUpButon;
    FirebaseAuth auth;
    AppCompatCheckedTextView signInGecis;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        tanimlamalar();

    }

    public void tanimlamalar(){
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        signUpButon=findViewById(R.id.signUpButon);
        signInGecis=findViewById(R.id.signInGecis);
        auth=FirebaseAuth.getInstance();

        signInGecis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();

            }
        });

        signUpButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailEditText.getText().toString();
                String pass=passwordEditText.getText().toString();

                if(!email.equals("") && !pass.equals("")){  // editextler de boş alan yoksa

                    emailEditText.setText(""); // edittexleri boşaltma
                    passwordEditText.setText("");
                    kayitOl(email,pass);  // methodu çalıştırma
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Fill in all fields!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // sisteme kayıt olma methodu
    public void kayitOl(String email,String pass){
        Log.i("hata",""+auth);
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // kayıt başarılıysa activity geçişi
                if(task.isSuccessful()){

                    // kullanıcı kayıtlarını veri tabanına kaydetme işlemleri
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    // 1. child tablo ismi, 2. childkullanıcı uid
                    reference=firebaseDatabase.getReference().child("Kullanicilar").child(auth.getUid());
                    Map map=new HashMap();
                    map.put("isim","null");
                    map.put("resim","null");
                    map.put("hakkinda","null");
                    map.put("telefon","null");
                   reference.setValue(map);

                    Log.i("hatamap",""+map);

                    Intent intent=new Intent(SignUpActivity.this, ButtomNavigationActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), "There was a problem!!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}