package com.example.oopsmelis.other;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oopsmelis.R;
import com.example.oopsmelis.activity.ChatActivity;
import com.example.oopsmelis.utilss.ProfileViewModel;
import com.example.oopsmelis.utilss.ShowToastMessage;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class OtherProfileFragment extends Fragment {


    View view;
    String otherId,userId;
    TextView userProfilTelephone,userProfilNameText,userProfilAboutMe,userProfilTakipText,userProfilTakipciText,
            userProfilArkadasEkleTextView,begenisayi;
    ImageView userProfilMesajImage,userProfilBegenImage,userProfilArkadasImage;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference,reference_Arkadaslik;
    CircleImageView userProfileimage;
    FirebaseAuth auth;
    FirebaseUser user;
    String kontrol="", begeniKontrol="";  // arkadaşlık istek gönderim kontrolü
    ShowToastMessage showToastMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=  inflater.inflate(R.layout.fragment_other_profile, container, false);

        tanimlamalar();
        action();
        getBegeniTextler();
        getArkadasTextler();

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
        userProfilBegenImage=view.findViewById(R.id.userProfilBegenImage);
        userProfilArkadasImage=view.findViewById(R.id.userProfilArkadasImage);
        userProfileimage=view.findViewById(R.id.userProfileimage);
        userProfilArkadasEkleTextView=view.findViewById(R.id.userProfilArkadasEkleTextView);
        showToastMessage=new ShowToastMessage(getContext());
        begenisayi=view.findViewById(R.id.begenisayi);


                                         //    TÜM KONTROLLER
        // otherid ye ait kişide(profiline girdiğimiz kişide ) bize ait arkadaşlık isteği var mı kontrolü
        reference_Arkadaslik.child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(userId)){                    // profiline girdiğimiz kişide db de bizim userid miz varmı kontrolu
                    // veri tabnındaki tip değerini kontrol değişkenine atadık
                    kontrol="istek";
                    userProfilArkadasImage.setImageResource(R.drawable.takip_okey); // istek var ise resmi değiştir(koyu yap)
                }else{
                    userProfilArkadasImage.setImageResource(R.drawable.takip_offf);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.child("Arkadaslar").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(otherId)) {
                    kontrol="arkadas";
                    userProfilArkadasImage.setImageResource(R.drawable.arkadas_sil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("Begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(userId)) {
                    begeniKontrol = "begendi";
                    userProfilBegenImage.setImageResource(R.drawable.begen2);
                }else{
                    userProfilBegenImage.setImageResource(R.drawable.begen1);
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

                if(!kullanicilar.getResim().equals("null")) {
                    Picasso.get().load(kullanicilar.getResim()).into(userProfileimage);
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

                if(kontrol == "istek"){
                    arkadaslikIptalEt(otherId,userId);
                }else if(kontrol == "arkadas"){

                    arkadasTablosundanCikar(otherId,userId);
                }else  {

                   arkadasEkle(otherId, userId);
               }

           }
        });

        userProfilBegenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(begeniKontrol.equals("begendi")){
                    begeniIptal(userId,otherId);
                }else {

                    begen(userId,otherId);
                }
            }
        });

        // mesaj gönderme
        userProfilMesajImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("username",userProfilArkadasEkleTextView.getText().toString());
                intent.putExtra("id",otherId);
                startActivity(intent);
            }
        });

    }

    private void begeniIptal(String userId, String otherId) {

        reference.child("Begeniler").child(otherId).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                userProfilBegenImage.setImageResource(R.drawable.begen1);
                begeniKontrol="";
                showToastMessage.showToast("Like canceled");
                getBegeniTextler();
            }
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

                                        kontrol="istek";
                                        showToastMessage.showToast("Friend request sent");  // toast mesajımız
                                        userProfilArkadasImage.setImageResource(R.drawable.takip_okey); // istek var ise resmi değiştir(koyu yap)


                                    }   else{
                                        showToastMessage.showToast("There is a problem");

                                    }
                                }
                            });

                }else{
                    showToastMessage.showToast("There is a problem");

                }

            }
        });

    }

    // arkadaşlık isteğini geri çekme ve veri tabanından silme işlemi
    public void arkadaslikIptalEt(final String otherId,final String userId){

        // arkadaş istek tablosunda otherid altındaki userid kaydını silme
        reference_Arkadaslik.child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               // arkadaş tablosunda userid altındaki otherid kaydını silme
                reference_Arkadaslik.child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol="";
                        userProfilArkadasImage.setImageResource(R.drawable.takip_offf);
                        showToastMessage.showToast("Friend request canceled");

                    }
                });
            }
        });

    }
    public void arkadasTablosundanCikar(final String otherId,final String userId){


        // arkadaş  tablosunda otherid altındaki userid kaydını silme
        reference.child("Arkadaslar").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // arkadaş tablosunda userid altındaki otherid kaydını silme
                reference.child("Arkadaslar").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol="";
                        userProfilArkadasImage.setImageResource(R.drawable.takip_offf);
                        showToastMessage.showToast("Unfriended !!");
                        getArkadasTextler();

                    }
                });
            }
        });
    }

    public void begen(String userId,String otherId){
        reference.child("Begeniler").child(otherId).child(userId).child("tip").setValue("begendi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                showToastMessage.showToast("Like the profile");
                userProfilBegenImage.setImageResource(R.drawable.begen2);
                begeniKontrol="begendi";
                getBegeniTextler();
            }
        });
    }

    public void getBegeniTextler(){

      //  List<String> begeniList=new ArrayList<>();

        reference.child("Begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //   begeniList.add(snapshot.getKey());
                // getchildrencount için addlistenerforsinglevalueevent kullanılmalıdır
                begenisayi.setText(snapshot.getChildrenCount()+" Like");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getArkadasTextler(){

       // List<String> arkadasList=new ArrayList<>();

        reference.child("Arkadaslar").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //  arkadasList.add(snapshot.getKey());
                userProfilTakipciText.setText(snapshot.getChildrenCount()+" Follower");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}