package com.example.oopsmelis.users;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oopsmelis.R;
import com.example.oopsmelis.utilss.ProfileViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class OtherProfileFragment extends Fragment {


    View view;
    String otherId,userId;
    TextView userProfilTelephone,userProfilNameText,userProfilAboutMe,userProfilTakipText,userProfilTakipciText,userProfilArkadasEkleTextView;
    ImageView userProfilMesajImage,userProfilTakipImage,userProfilArkadasImage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference,reference_Arkadaslik;
    CircleImageView userProfileimage;
    FirebaseAuth auth;
    FirebaseUser user;
    String kontrol;  // arkadaşlık istek gönderim kontrolü

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=  inflater.inflate(R.layout.fragment_other_profile, container, false);

        tanimlamalar();
        action();

        return view;

    }

    public void tanimlamalar() {

        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userId=user.getUid();  // istek gönderecek kişinin id sini alma
        // istek tablosu oluşturma işlemi
        reference_Arkadaslik=firebaseDatabase.getReference().child("Arkadaslik_Istek");
        // homefragmentten gönderilen recyclerview de tıklanılan item ın userid sini alıp otherid ye atma işlemi
        otherId = getArguments().getString("userid");
        userProfilTelephone=view.findViewById(R.id.userProfilTelephone);
        userProfilNameText=view.findViewById(R.id.userProfilNameText);
        userProfilAboutMe=view.findViewById(R.id.userProfilAboutMe);
        userProfilTakipText=view.findViewById(R.id.userProfilTakipText);
        userProfilTakipciText=view.findViewById(R.id.userProfilTakipciText);
        userProfilMesajImage=view.findViewById(R.id.userProfilMesajImage);
        userProfilTakipImage=view.findViewById(R.id.userProfilTakipImage);
        userProfilArkadasImage=view.findViewById(R.id.userProfilArkadasImage);
        userProfileimage=view.findViewById(R.id.userProfileimage);
        userProfilArkadasEkleTextView=view.findViewById(R.id.userProfilArkadasEkleTextView);


        // otherid ye ait kişide(profiline girdiğimiz kişide ) bize ait arkadaşlık isteği var mı kontrolü
        reference_Arkadaslik.child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(userId)){                    // profiline girdiğimiz kişide db de bizim userid miz varmı kontrolu
                    // veri tabnındaki tip değerini kontrol değişkenine atadık
                    kontrol=snapshot.child(userId).child("tip").getValue().toString();
                    userProfilArkadasImage.setImageResource(R.drawable.takip_okey); // istek var ise resmi değiştir(koyu yap)
                }else{
                    userProfilArkadasImage.setImageResource(R.drawable.takip_offf);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public void action(){

        reference.child("Kullanicilar").child(otherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ProfileViewModel kullanicilar=snapshot.getValue(ProfileViewModel.class);
                // setlemeler
                userProfilTelephone.setText("Telephone : "+kullanicilar.getTelefon());
                userProfilNameText.setText("Name-Surname : "+kullanicilar.getIsim());
                userProfilAboutMe.setText("About Me : "+kullanicilar.getHakkinda());
                userProfilArkadasEkleTextView.setText(kullanicilar.getIsim());

                if(!kullanicilar.getResim().equals("")) {
                 //   Picasso.get().load(kullanicilar.getResim()).into(userProfileimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // arkadalık isteği gönderme image i
        userProfilArkadasImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*                if(!kontrol.equals(" ")){
                    arkadaslikIptalEt(otherId,userId);
                }else {

  */                  arkadasEkle(otherId, userId);
               }

    //        }
        });

    }

    public void arkadasEkle(final String otherId,final String userId){

        // otherid kişisine istek gönderme
        reference_Arkadaslik.child(userId).child(otherId).child("tip").setValue("gonderdi")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                         // otherid kişisi tarafından arkadaşlık isteğinin alınması
                    reference_Arkadaslik.child(otherId).child(userId).child("tip").setValue("aldi")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        kontrol="aldi";
                                        Toast.makeText(getContext(), "Friend request sent", Toast.LENGTH_LONG).show();
                                        userProfilArkadasImage.setImageResource(R.drawable.takip_okey); // istek var ise resmi değiştir(koyu yap)

                                    }   else{
                                        Toast.makeText(getContext(), "There is a problem", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }else{
                    Toast.makeText(getContext(), "There is a problem", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    // arkadaşlık isteğini geri çekme ve veri tabanından silme işlemi
    public void arkadaslikIptalEt(final String otherId,final String userId){

        // arkadaş tablosunda otherid altındaki userid kaydını silme
        reference_Arkadaslik.child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               // arkadaş tablosunda userid altındaki otherid kaydını silme
                reference_Arkadaslik.child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol="";
                        userProfilArkadasImage.setImageResource(R.drawable.takip_offf);
                        Toast.makeText(getContext(), "Friend request canceled", Toast.LENGTH_LONG).show();

                    }
                });
            }
        });

    }

}