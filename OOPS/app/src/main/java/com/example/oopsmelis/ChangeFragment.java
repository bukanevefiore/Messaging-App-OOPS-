package com.example.oopsmelis;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ChangeFragment extends Fragment {

    private Context context;

    public ChangeFragment(Context context) {
        this.context = context;
    }

   public void change(Fragment fragment){
       ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
               .replace(R.id.nav_host_fragment,fragment,"fragment")
               .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
               .commit();
   }

   public void changeWithParemeter(Fragment fragment,String userid){

        // geçilecek fragmente userıd yi paremetre olarak gönderme işlemi
       Bundle bundle=new Bundle();
       bundle.putString("userid",userid);
       fragment.setArguments(bundle);

       ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
               .replace(R.id.nav_host_fragment,fragment,"fragment")
               .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
               .commit();
   }
}