package com.example.oopsmelis.utilss;

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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<String> userKeysList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String userId;
    List<MessageModel> messageModelList;
    // mesajların sağda solda bulunma durumu
    Boolean state;
    int view_type_send=1,view_type_recived=2;

    // constructor
    public MessageAdapter(List<String> userKeysList, Activity activity, Context context,List<MessageModel> messageModelList) {
        this.userKeysList = userKeysList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
        this.messageModelList=messageModelList;
        state=false;

    }


    // recyclerview implemente methodlar
    // layout tanımlaması yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        // mesajların giden gelen şeklinde sağa sola yaslanması için
        if(viewType==view_type_send){

            view= LayoutInflater.from(context).inflate(R.layout.message_sent_layout, parent, false);
            // Viewholder clasımıza view i gönderiyoruz ki viewlerden haberdar olsun
            return new ViewHolder(view);

        }else{

            view= LayoutInflater.from(context).inflate(R.layout.message_received_layout, parent, false);
            // Viewholder clasımıza view i gönderiyoruz ki viewlerden haberdar olsun
            return new ViewHolder(view);
        }

    }

    // Vievholder iç class ımız (View leri tanımlama sınıfımız )
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;

        ViewHolder(View itemView) {
            super(itemView);

            if(state==true)
            {
                messageText = itemView.findViewById(R.id.message_sent);
            }
            else{
                messageText = itemView.findViewById(R.id.message_received);
            }





        }

    }

    // view lere setlemeler yapılacak ( from child ı ile belirlenecek)
    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {

        holder.messageText.setText(messageModelList.get(position).getText());

    }


    // adapter oluşturulacak olan listenin size
    @Override
    public int getItemCount() {
        return messageModelList.size();
    }


// mesajın sağa sola mı hizzalı olduğunu belirleme
    @Override
    public int getItemViewType(int position) {

        if(messageModelList.get(position).getFrom().equals(userId)){

            state=true;
            return view_type_send;

        }else{

            state=false;
            return view_type_recived;

        }

    }


}
