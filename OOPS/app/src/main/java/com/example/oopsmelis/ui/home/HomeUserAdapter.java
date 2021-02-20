package com.example.oopsmelis.ui.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oopsmelis.ChangeFragment;
import com.example.oopsmelis.R;
import com.example.oopsmelis.users.OtherProfileFragment;
import com.example.oopsmelis.utilss.ProfileViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeUserAdapter extends RecyclerView.Adapter<HomeUserAdapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;



    // constructor
    public HomeUserAdapter(List<String> userKeysList, Activity activity, Context context) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
    }




    // recyclerview implemente methodlar
    // layout tanımlaması yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false);
        // Viewholder clasımıza view i gönderiyoruz ki viewlerden haberdar olsun
        return new ViewHolder(view);
    }

    // Vievholder iç class ımız (View leri tanımlama sınıfımız )
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTextView;
        CircleImageView userImage,user_state_img;
        LinearLayout userHomeLinearLayout;

        ViewHolder(View itemView){
            super(itemView);
            usernameTextView=itemView.findViewById(R.id.usernameTextView);
            userImage=itemView.findViewById(R.id.userImage);
            userHomeLinearLayout=itemView.findViewById(R.id.userHomeLinearLayout);
            user_state_img=itemView.findViewById(R.id.user_state_img);


        }

    }

    // view lere setlemeler yapılacak
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {

      //  holder.usernameTextView.setText(userKeysList.get(position).toString());

        // veritabanından child mantığıyla kullanicilarin isimlerini alma(database e bağlanma)
        reference.child("Kullanicilar").child(userKeysList.get(position).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ProfileViewModel kullanicilar=snapshot.getValue(ProfileViewModel.class);

                Boolean stateUser=Boolean.parseBoolean(snapshot.child("state").getValue().toString());

                if(stateUser==true){
                    holder.user_state_img.setImageResource(R.drawable.yesil_circle);
                }else{
                    holder.user_state_img.setImageResource(R.drawable.red_circle);
                }

                     Picasso.get().load(kullanicilar.getResim()).into(holder.userImage);
                     holder.usernameTextView.setText(kullanicilar.getIsim());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // homefragment de kullanıcılardan birine tıkladığımızda kullanıcı ayrıntı bilgilere gitme
        holder.userHomeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangeFragment changeFragment=new ChangeFragment(context);
                changeFragment.changeWithParemeter(new OtherProfileFragment(),userKeysList.get(position));

            }
        });

    }

    // adapter oluşturulacak olan listenin size
    @Override
    public int getItemCount() {
        return userKeysList.size();
    }





}
