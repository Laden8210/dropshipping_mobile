package com.example.dropshipping.view;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dropshipping.R;
import com.example.dropshipping.fragment.AccountFragment;
import com.example.dropshipping.fragment.CartFragment;
import com.example.dropshipping.fragment.NotificationFragment;
import com.example.dropshipping.fragment.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HeroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hero);


        BottomNavigationView bnvHero = findViewById(R.id.bnvHero);

        if (savedInstanceState == null) {
            loadFragment(new ShopFragment());
        }

        bnvHero.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.menu_shop) {
                selectedFragment = new ShopFragment();
                return loadFragment(selectedFragment);
            }
            if (item.getItemId() == R.id.menu_cart) {
                selectedFragment = new CartFragment();
                return loadFragment(selectedFragment);
            }
            if (item.getItemId() == R.id.menu_notification) {
                selectedFragment = new NotificationFragment();
                return loadFragment(selectedFragment);
            }
            if (item.getItemId() == R.id.menu_account) {
                selectedFragment = new AccountFragment();
                return loadFragment(selectedFragment);
            }


            return false;

        });


    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nfvHero, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }
}