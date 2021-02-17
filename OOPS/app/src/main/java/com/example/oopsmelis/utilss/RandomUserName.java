package com.example.oopsmelis.utilss;

import java.util.Random;

public class RandomUserName { // her yeni kullanıcı için random isim üretme classı

    // static olduğu için Profilefragment inde nesne oluşturmdan direk clas ismiyle kullanılabiecek
    public static String getSaltString(){
        String SALTCHARS="ABCDEFGHIJKLMNOPRSTUVYZ1234567890";
        StringBuilder salt=new StringBuilder();
        Random rnd=new Random();
        while (salt.length() < 18) {
            int index=(int)(rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr=salt.toString();
        return saltStr;
    }
}
