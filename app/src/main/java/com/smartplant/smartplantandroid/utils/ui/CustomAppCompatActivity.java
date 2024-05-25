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

    // With animation
    protected void replaceFragment(int fragmentId, Fragment fragment, int enterAnim, int exitAnim, int popEnterAnim, int popExitAnim) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);

        fragmentTransaction.replace(fragmentId, fragment);
        fragmentTransaction.commit();
    }

    // With animation array
    protected void replaceFragment(int fragmentId, Fragment fragment, int[] animation) {
        if (animation == null || animation.length != 4) throw new IllegalArgumentException("Animations array must have exactly 4 elements.");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(animation[0], animation[1], animation[2], animation[3]);

        fragmentTransaction.replace(fragmentId, fragment);
        fragmentTransaction.commit();
    }
}
