package com.example.oopsmelis.utilss;

import android.content.Context;
import android.widget.Toast;

public class ShowToastMessage {

    // toast mesajların gösterilmesi için sınıf
    Context context;


    public ShowToastMessage(Context context) {
        this.context = context;

    }

    public void showToast(String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
