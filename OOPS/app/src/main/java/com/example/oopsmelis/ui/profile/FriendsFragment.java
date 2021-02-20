package com.example.oopsmelis.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oopsmelis.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment {

    View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    RecyclerView friendsListRecyclerViev;
    FriendsAdapter friendsAdapter;
    List<String> friendsList;
    FirebaseUser user;
    FirebaseAuth auth;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_friends, container, false);

        tanimlamalar();
        getFriendsList();

        return view;
    }

    public void tanimlamalar(){

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userId=user.getUid();

        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference().child("Arkadaslar");
        friendsList=new ArrayList<>();
        friendsListRecyclerViev=view.findViewById(R.id.friendsListRecyclerViev);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext());
        friendsListRecyclerViev.setLayoutManager(layoutManager);
        friendsAdapter=new FriendsAdapter(friendsList,getActivity(),getContext());
        friendsListRecyclerViev.setAdapter(friendsAdapter);


    }

    public void getFriendsList(){

        reference.child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                friendsList.add(snapshot.getKey());
                friendsAdapter.notifyDataSetChanged();

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