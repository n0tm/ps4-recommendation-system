package com.example.playstationsearchjava;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.playstationsearchjava.Fragments.FavoriteGamesFragment;
import com.example.playstationsearchjava.Fragments.SearchFragment;
import com.example.playstationsearchjava.Fragments.TopGamesFragment;

public class MenuActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.favorite:
                        fragment = new FavoriteGamesFragment();
                        break;
                    case R.id.top_games:
                        fragment = new TopGamesFragment();
                        break;
//                    case R.id.profile:
//                        fragment = new FavoriteGamesFragment();
//                        break;
                    case R.id.search:
                        fragment = new SearchFragment();
                        break;
                }

                return loadFragment(fragment);
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        loadFragment(new FavoriteGamesFragment());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
