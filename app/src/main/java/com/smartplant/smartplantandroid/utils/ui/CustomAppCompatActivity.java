package com.smartplant.smartplantandroid.utils.ui;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class CustomAppCompatActivity extends AppCompatActivity {
    protected void startActivity(Class<?> newActivity) {
        Intent intent = new Intent(this, newActivity);
        startActivity(intent);
    }

    protected void replaceFragment(int fragmentId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentId, fragment);
        fragmentTransaction.commit();
    }
}
