package com.mostafabor3e.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mostafabor3e.insta.Fragment.Home;
import com.mostafabor3e.insta.Fragment.Notifiection;
import com.mostafabor3e.insta.Fragment.Profile;
import com.mostafabor3e.insta.Fragment.Search;

public class MainActivity extends AppCompatActivity {
BottomNavigationView bottomNavigationView;
Fragment fragment;
public static final  String userid="id";
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.ButtonNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    getSupportFragmentManager().beginTransaction().replace(R.id.fragmet_continer,new Home()).commit();

}
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    fragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.Home:
                            fragment = new Home();
                            break;
                        case R.id.search:
                            fragment = new Search();
                            break;
                        case R.id.add:
                            fragment = null;
                            startActivity(new Intent(getBaseContext(), Poast.class));
                            break;
                        case R.id.fav:
                            fragment = new Notifiection();
                            break;

                        case R.id.profile:

                            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            fragment = new Profile();
                            break;
                    }
                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmet_continer, fragment).commit();

                    }
                    return true;
                }
            };
}