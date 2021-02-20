package com.example.oopsmelis.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oopsmelis.R;
import com.example.oopsmelis.utilss.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String userId;

    // constructor
    public FriendsAdapter(List<String> userKeysList, Activity activity, Context context) {
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

        View view = LayoutInflater.from(context).inflate(R.layout.friends_layout, parent, false);
        // Viewholder clasımıza view i gönderiyoruz ki viewlerden haberdar olsun
        return new ViewHolder(view);
    }

    // Vievholder iç class ımız (View leri tanımlama sınıfımız )
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView friend_nametext;
        CircleImageView friend_prof_img,friend_state_img;
        LinearLayout userHomeLinearLayout;
        Button kabulEtButon, reddetButon;

        ViewHolder(View itemView) {
            super(itemView);
            friend_nametext = itemView.findViewById(R.id.friend_nametext);
            friend_prof_img = itemView.findViewById(R.id.friend_prof_img);
            friend_state_img = itemView.findViewById(R.id.friend_state_img);
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

               String userName=snapshot.child("isim").getValue().toString();
               String userImage=snapshot.child("resim").getValue().toString();
               Boolean stateUser=Boolean.parseBoolean(snapshot.child("state").getValue().toString());

               if(stateUser==true){
                   holder.friend_state_img.setImageResource(R.drawable.yesil_circle);
               }else{
                   holder.friend_state_img.setImageResource(R.drawable.red_circle);
               }

                Picasso.get().load(userImage).into(holder.friend_prof_img);
                holder.friend_nametext.setText(userName);




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



}
