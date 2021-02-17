package com.example.oopsmelis.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.oopsmelis.R;
import com.example.oopsmelis.utilss.ProfileViewModel;
import com.example.oopsmelis.utilss.RandomUserName;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    View root;
    private TextView textView;
    private TextInputEditText namee,hakkimda,tel;
    Button bilgiGuncelle;
    String imageUrl;
    CircleImageView profile_image;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    Activity activity;
    StorageReference storageReference;
   FirebaseStorage firebaseStorage;  // profil resmini firebase e kaydetmek için

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        tanimlamalar();
        bilgileriGetir();

        // bilgileri güncelleme
        bilgiGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= namee.getText().toString();
                String hakkindaa= hakkimda.getText().toString();
                String telefonn= tel.getText().toString();



                reference=firebaseDatabase.getReference().child("Kullanicilar").child(auth.getUid());
                Map map=new HashMap();

                map.put("isim",name);
                map.put("hakkinda",hakkindaa);
                map.put("telefon",telefonn);
                if(imageUrl.equals("null"))
                {
                    map.put("resim","null");
                }else
                    {

                        map.put("resim",imageUrl);
                }

                // yeni değerlerin set edilmesi işlemi
                reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            root = inflater.inflate(R.layout.fragment_profile, container, false);
                            Toast.makeText(getContext(), "Information updated !!", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Update failed !!", Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
        });


        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    public void tanimlamalar(){

        // fragment tanimlamalar
        textView = root.findViewById(R.id.bilgiTextView);
        namee = root.findViewById(R.id.namee);
        hakkimda = root.findViewById(R.id.hakkimda);
        tel = root.findViewById(R.id.tel);
        bilgiGuncelle=root.findViewById(R.id.bilgiGuncelle);
       // profil rsmi kaydetme nesneleri tanımlama
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        profile_image=root.findViewById(R.id.profile_image);

        // profil resmini değiştirme
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                galeriAc();
            }
        });

        // veritabanı tanimlamalar
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference().child("Kullanicilar").child(user.getUid());
    }

    // profil resmi değiştirmek için galeri açma işlemi
    public void galeriAc(){
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,5);
    }

    // galeriden seçtiğimiz resme ulaşma methodu
    public void onActivityResult(int requestCode,int resultCode, Intent data){

        if(requestCode==5 && resultCode==Activity.RESULT_OK){

            Uri filePath= data.getData(); // resim dosyasını galeriden aldık
            profile_image.setImageURI(filePath);
            Log.i("resimm","" +filePath);
            //String filee=filePath.toString();

            // resim dosyasına firebase e yükleme                                          // random isim üretme sınıfı
            StorageReference resimRef=storageReference.child("KullaniciResimleri").child(RandomUserName.getSaltString()+".jpg");
            resimRef.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(), "Image updated !!", Toast.LENGTH_LONG).show();
                        //namee.setText(filee);  deneme

                        // bilgi güncelleme
                        String name= namee.getText().toString();
                        String hakkindaa= hakkimda.getText().toString();
                        String telefonn= tel.getText().toString();

                        reference=firebaseDatabase.getReference().child("Kullanicilar").child(auth.getUid());
                        Map map=new HashMap();

                        map.put("isim",name);
                        map.put("hakkinda",hakkindaa);
                        map.put("telefon",telefonn);
                        // galeriden aldığımız resmi güncelleme (url ini firebase e yazdırma)
                        map.put("resim",task.getResult().getUploadSessionUri().toString());

                        //String aa=task.getResult().getUploadSessionUri().toString();
                        // namee.setText(aa);


                        // yeni değerlerin set edilmesi işlemi
                        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    //root = inflater.inflate(R.layout.fragment_profile, container, false);
                                    Toast.makeText(getContext(), "Information updated !!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Update failed !!", Toast.LENGTH_LONG).show();
                                }

                            }
                        });


                    }else
                    {

                        Toast.makeText(getContext(), "Image failed !!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
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
                imageUrl=kullanicilar.getResim();
               // if(!kullanicilar.getResim().equals("null")){  // resim kontrolü

                try {
                    Picasso.get().load(kullanicilar.getResim()).into(profile_image);
                    Log.d("resimcek","hata varrrrr");

                }catch (Exception e){
                    Log.d("resimcek",e.getMessage());
                }

               // }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("bilgicekdehata",error.getMessage());

            }
        });

    }


}