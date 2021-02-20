package com.example.oopsmelis.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.oopsmelis.R;
import com.example.oopsmelis.utilss.GetDate;
import com.example.oopsmelis.utilss.MessageAdapter;
import com.example.oopsmelis.utilss.MessageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.awt.font.TextAttribute;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private TextView mesajUserNameText;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    FloatingActionButton mesajGonderButon;
    TextInputEditText mesajEditText;
    List<MessageModel> messageModelList;
    RecyclerView chat_recycler_view;
    MessageAdapter adapter;
    List<String> keyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tanimlamalar();
        action();
        messageLoad();

    }

    // otherprofil fragmentinden username gönderilen paremetresinin alınması
    public String getUserName(){

        String userName = getIntent().getExtras().getString("username");
        return userName;
    }

    // otherprofil fragmentinden id gönderilen paremetresinin alınması
    public String getId(){

        String id = getIntent().getExtras().getString("id").toString();
        return id;
    }

    public void tanimlamalar(){

        mesajUserNameText=findViewById(R.id.mesajUserNameText);
        mesajUserNameText.setText(getUserName());
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        mesajGonderButon=findViewById(R.id.mesajGonderButon);
        mesajEditText=findViewById(R.id.mesajEditText);
        messageModelList=new ArrayList<>();

        keyList=new ArrayList<>();
        chat_recycler_view=findViewById(R.id.chat_recycler_view);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ChatActivity.this);
        chat_recycler_view.setLayoutManager(layoutManager);
        adapter=new MessageAdapter(keyList,ChatActivity.this,ChatActivity.this,messageModelList);
        chat_recycler_view.setAdapter(adapter);

    }

    // mesaj gönderme işlemi
    public void sendMessage(final String userId,final String otherId,String textType,String date,Boolean seen,String messageText){


        String mesajId=reference.child("Mesajlar").child(userId).child(otherId).push().getKey();

        // mesajları mesaj bloğuna yazma
        Map messageMap=new HashMap();
        messageMap.put("type",textType);
        messageMap.put("time",date);
        messageMap.put("seen",seen);
        messageMap.put("text",messageText);
        messageMap.put("from",userId);

        reference.child("Mesajlar").child(userId).child(otherId).child(mesajId).setValue(messageMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        reference.child("Mesajlar").child(otherId).child(userId).child(mesajId).setValue(messageMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                    }
                });
    }

    public void action(){


        mesajGonderButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String message=mesajEditText.getText().toString();
                sendMessage(user.getUid(),getId(),"text", GetDate.getDate(),false,message);
                mesajEditText.setText("");

            }
        });
    }

    // mesajları listeleme
    public void messageLoad(){

        reference.child("Mesajlar").child(user.getUid()).child(getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                MessageModel messageModel=snapshot.getValue(MessageModel.class);
                messageModelList.add(messageModel);
                adapter.notifyDataSetChanged();
                keyList.add(snapshot.getKey());
                // chat activity de anlık atılan mesajın alt tarafta kalmaması için
                chat_recycler_view.scrollToPosition(messageModelList.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}