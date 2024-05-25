package com.smartplant.smartplantandroid.utils.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class CustomAppCompatActivity extends AppCompatActivity {
    protected void startNewActivity(Class<?> newActivity) {
        Intent intent = new Intent(this, newActivity);
        startActivity(intent);
    }

    protected void startNewActivity(Class<?> newActivity, Bundle extras) {
        Intent intent = new Intent(this, newActivity);
        intent.putExtras(extras);
        startActivity(intent);
    }

    protected void replaceFragment(int fragmentId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentId, fragment);
        fragmentTransaction.commit();
    }
}
