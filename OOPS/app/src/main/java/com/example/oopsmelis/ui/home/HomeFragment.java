package com.example.oopsmelis.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oopsmelis.R;
import com.example.oopsmelis.utilss.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    List<String> userKeysList;
    View root;
    RecyclerView userListRecyclerView;
    HomeUserAdapter homeUserAdapter;
    FirebaseAuth auth;
    FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       // homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);

        tanimlamalar();
        kullanicilariGetir();

     /*   homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

      */
        return root;
    }

    public void tanimlamalar(){

        userKeysList=new ArrayList<>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        userListRecyclerView=root.findViewById(R.id.userListRecyclerViev);
        RecyclerView.LayoutManager mng=new LinearLayoutManager(getContext());  // recycler view oluştu
        userListRecyclerView.setLayoutManager(mng);  // listemizi recyclerview e set ettik
        homeUserAdapter=new HomeUserAdapter(userKeysList,getActivity(),getContext());
        userListRecyclerView.setAdapter(homeUserAdapter);     // adapter i view e set ettik
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }

    public void kullanicilariGetir(){

        reference.child("Kullanicilar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                // veritabanından child mantığıyla kullanicilarin isimlerini alma
                reference.child("Kullanicilar").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        ProfileViewModel kullanicilar=snapshot.getValue(ProfileViewModel.class);
                        // kullanıcı ismi null olmayan ve mevcut hesap dışındaki hesapları listeliyoruz
                        if(!kullanicilar.getIsim().equals("") && !snapshot.getKey().equals(user.getUid()))
                        {

                            // kullanıcı isimleri null olmayanları listeye ekleyip home fragmentinde gösteriyoruz
                            Log.i("keyler",snapshot.getKey());

                            //kullanıcı çoklamasını önlemek için if
                            if(userKeysList.indexOf(snapshot.getKey())==-1) {
                                // kullanıcılara ait tüm keyleri listemize ekliyoruz
                                userKeysList.add(snapshot.getKey());
                            }
                                homeUserAdapter.notifyDataSetChanged();  // adapteri anlık güncelleme

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


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