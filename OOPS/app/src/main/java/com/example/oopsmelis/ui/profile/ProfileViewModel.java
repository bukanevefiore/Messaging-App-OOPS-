package com.example.oopsmelis.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private String hakkinda, isim, resim, telefon;

    public ProfileViewModel(String hakkinda, String isim, String resim, String telefon) {
        this.hakkinda = hakkinda;
        this.isim = isim;
        this.resim = resim;
        this.telefon = telefon;
    }

    public String getHakkinda() {
        return hakkinda;
    }

    public void setHakkinda(String hakkinda) {
        this.hakkinda = hakkinda;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    @Override
    public String toString() {
        return "ProfileViewModel{" +
                "hakkinda='" + hakkinda + '\'' +
                ", isim='" + isim + '\'' +
                ", resim='" + resim + '\'' +
                ", telefon='" + telefon + '\'' +
                '}';
    }

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("General Information");
    }

    public LiveData<String> getText() {
        return mText;
    }
}