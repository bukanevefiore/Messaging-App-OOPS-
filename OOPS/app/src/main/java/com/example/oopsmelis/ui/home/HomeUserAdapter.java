package com.example.oopsmelis.ui.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oopsmelis.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeUserAdapter extends RecyclerView.Adapter<HomeUserAdapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;

    // constructor
    public HomeUserAdapter(List<String> userKeysList, Activity activity, Context context) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
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

        TextView isim;
        CircleImageView resim;

        ViewHolder(View itemView){
            super(itemView);
            isim=itemView.findViewById(R.id.usernameTextView);
            resim=itemView.findViewById(R.id.userImage);


        }

    }

    // view lere setlemeler yapılacak
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    // adapter oluşturulacak olan listenin size
    @Override
    public int getItemCount() {
        return 0;
    }





}
