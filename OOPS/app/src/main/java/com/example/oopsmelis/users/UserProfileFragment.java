package com.example.oopsmelis.users;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oopsmelis.R;


public class UserProfileFragment extends Fragment {

    TextView denemetext;
    View view;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        tanimlamalar();
        action();

        return view;
    }

    public void tanimlamalar() {

        denemetext = view.findViewById(R.id.denemetext);
        userId = getArguments().getString("userid");
    }

    public void action(){

        denemetext.setText(userId);
    }
}