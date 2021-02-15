package com.example.oopsmelis.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.oopsmelis.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class ButtomNavigationActivity extends AppCompatActivity {

    // henüz kullanıcı girişi olmamışsa uygulma kayıt ol ekranına yönlendirme
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttom_navigation);

        tanimlamalar();
        kontrol();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Her menü kimliğini bir kimlik kümesi olarak iletmek çünkü her biri
        // menü üst düzey hedefler olarak düşünülmelidir.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications,R.id.navigation_signOut)
                .build();



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);




       // navView.setOnNavigationItemReselectedListener(navListener);

    }
    /*
    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment=null;
                    switch (item.getItemId()){
                        case R.id.navigation_signOut:

                            auth.signOut();
                            Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                            startActivity(intent);
                            finish(); // signup activity e geçtikten sonra main activity e  gelmeyi engellemek için
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_container,selectedFragment).commit();
                    return true;
                }
            };

     */
    public void tanimlamalar(){
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
    }

    // kullanıcı varsa main activity de kalacak yoksa uygulama açıldığı gibi sign up activity açılacak
    public void kontrol(){

        if(user==null)  // kullanıcı yoksa
        {
            Intent intent=new Intent(ButtomNavigationActivity.this, SignInActivity.class);
            startActivity(intent);
            finish(); // signup activity e geçtikten sonra main activity e  gelmeyi engellemek için
        }else
        {

        }
    }

}