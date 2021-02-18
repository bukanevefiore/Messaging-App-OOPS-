package com.example.oopsmelis.utilss;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oopsmelis.ChangeFragment;
import com.example.oopsmelis.R;
import com.example.oopsmelis.users.OtherProfileFragment;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Calendar;


import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String userId;

    // constructor
    public FriendRequestAdapter(List<String> userKeysList, Activity activity, Context context) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();

    }


    // recyclerview implemente methodlar
    // layout tanımlaması yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.friend_request_layout, parent, false);
        // Viewholder clasımıza view i gönderiyoruz ki viewlerden haberdar olsun
        return new ViewHolder(view);
    }

    // Vievholder iç class ımız (View leri tanımlama sınıfımız )
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView friendRequestName;
        CircleImageView friendRequestImage;
        LinearLayout userHomeLinearLayout;
        Button kabulEtButon, reddetButon;

        ViewHolder(View itemView) {
            super(itemView);
            friendRequestName = itemView.findViewById(R.id.friendRequestName);
            friendRequestImage = itemView.findViewById(R.id.friendRequestImage);
            userHomeLinearLayout = itemView.findViewById(R.id.userHomeLinearLayout);
            kabulEtButon = itemView.findViewById(R.id.kabulEtButon);
            reddetButon = itemView.findViewById(R.id.reddetButon);


        }

    }

    // view lere setlemeler yapılacak
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {

        //  holder.usernameTextView.setText(userKeysList.get(position).toString());

        // veritabanından child mantığıyla kullanicilarin isimlerini alma(database e bağlanma)
        reference.child("Kullanicilar").child(userKeysList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ProfileViewModel kullanicilar = snapshot.getValue(ProfileViewModel.class);

                Picasso.get().load(kullanicilar.getResim()).into(holder.friendRequestImage);
                holder.friendRequestName.setText(kullanicilar.getIsim());

                holder.kabulEtButon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        istekKabulEt(userId, userKeysList.get(position));
                    }
                });

                holder.reddetButon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        istekReddet(userId, userKeysList.get(position));
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    // adapter oluşturulacak olan listenin size
    @Override
    public int getItemCount() {
        return userKeysList.size();
    }


    private void istekKabulEt(String userId, String otherId) {

        // anlık tarih alma
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);

        reference.child("Arkadaslar").child(userId).child(otherId).child("tarih").setValue(strDate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        reference.child("Arkadaslar").child(otherId).child(userId).child("tarih").setValue(strDate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "The transaction is successful", Toast.LENGTH_LONG).show();
                                            reference.child("Arkadaslik_Istek").child(userId).child(otherId).removeValue()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            reference.child("Arkadaslik_Istek").child(otherId).child(userId).removeValue()
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {


                                                                        }
                                                                    });

                                                        }
                                                    });
                                        }
                                    }
                                });
                    }
                });

    }

    private void istekReddet(String userId, String otherId) {

        reference.child("Arkadaslik_Istek").child(userId).child(otherId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        reference.child("Arkadaslik_Istek").child(otherId).child(userId).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(context, "Delete Success", Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                });

    }

}
