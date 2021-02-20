package com.example.oopsmelis.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oopsmelis.R;
import com.example.oopsmelis.other.FriendRequestAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    View root;
    FirebaseUser user;
    FirebaseAuth auth;
    String userId;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<String> friend_req_key_list;
    RecyclerView friendRequestListRecyclerViev;
    FriendRequestAdapter adapter;
    String kontrol;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        root = inflater.inflate(R.layout.fragment_notifications, container, false);

        tanimlamalar();
        istekler();

        return root;
    }

    public void tanimlamalar(){

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userId=user.getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference().child("Arkadaslik_Istek");
        friend_req_key_list=new ArrayList<>();
        friendRequestListRecyclerViev=root.findViewById(R.id.friendRequestListRecyclerViev);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        friendRequestListRecyclerViev.setLayoutManager(layoutManager);
        adapter=new FriendRequestAdapter(friend_req_key_list,getActivity(),getContext());
        friendRequestListRecyclerViev.setAdapter(adapter);
    }

    public void istekler(){

        reference.child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                    Log.i("istekler", snapshot.getKey());
                    kontrol = snapshot.child("tip").getValue().toString();
                    if (kontrol.equals("aldi")) {
                        // bildirimde kullanıcı çoklamasına engel olmak için if
                        if(friend_req_key_list.indexOf(snapshot.getKey())==-1) {
                            friend_req_key_list.add(snapshot.getKey());
                        }
                        adapter.notifyDataSetChanged();
                    }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                friend_req_key_list.remove(snapshot.getKey());
                adapter.notifyDataSetChanged();
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