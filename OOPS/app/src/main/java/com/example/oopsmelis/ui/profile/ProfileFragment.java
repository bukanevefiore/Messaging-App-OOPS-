package com.example.oopsmelis.ui.profile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.oopsmelis.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    View root;
    private TextView textView;
    private TextInputEditText namee,hakkimda,tel;
    Button bilgiGuncelle;
    CircleImageView profile_image;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Activity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        tanimlamalar();
        bilgileriGetir();


        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    public void tanimlamalar(){

        textView = root.findViewById(R.id.bilgiTextView);
        namee = root.findViewById(R.id.namee);
        hakkimda = root.findViewById(R.id.hakkimda);
        tel = root.findViewById(R.id.tel);
        bilgiGuncelle=root.findViewById(R.id.bilgiGuncelle);
        bilgiGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guncelle();
            }
        });

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference().child("Kullanicilar").child(user.getUid());
    }

    public void bilgileriGetir(){

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ProfileViewModel kullanicilar=snapshot.getValue(ProfileViewModel.class);
                Log.i("kullanıcı bilgiler",kullanicilar.toString());
/*
                String adi=snapshot.child("isim").getValue().toString();
                String hakkinda=snapshot.child("hakkinda").getValue().toString();
                String telefon=snapshot.child("telefon").getValue().toString();
                String resim=snapshot.child("resim").getValue().toString();
*/

                // set işlemleri
                namee.setText(kullanicilar.getIsim());
                hakkimda.setText(kullanicilar.getHakkinda());
                tel.setText(kullanicilar.getTelefon());
                if(!kullanicilar.getResim().equals("")){  // resim kontrolü

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void guncelle(){

       String name= namee.getText().toString();
       String hakkindaa= hakkimda.getText().toString();
       String telefonn= tel.getText().toString();

        reference=firebaseDatabase.getReference().child("Kullanicilar").child(auth.getUid());
        Map map=new HashMap();

        map.put("isim",name);
        map.put("hakkinda",hakkindaa);
        map.put("telefon",telefonn);
        // yeni değerlerin set edilmesi işlemi
        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

               // profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
               // root = inflater.inflate(R.layout.fragment_profile, container, false);

            }
        });

    }
}