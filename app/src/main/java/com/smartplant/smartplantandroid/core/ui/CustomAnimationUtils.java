package com.smartplant.smartplantandroid.core.ui;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class CustomAnimationUtils {
    public static void replaceFragmentWithFullAnimation(
            @NonNull Context context,
            @NonNull FragmentManager fragmentManager,
            @Nullable Fragment currentFragment,
            @NonNull Fragment newFragment,
            @IdRes int containerId,
            @AnimRes int enterAnimation,
            @AnimRes int exitAnimation) {

        if (currentFragment == null) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(enterAnimation, 0)
                    .replace(containerId, newFragment)
                    .commit();
            return;
        }

        Animation exitAnim = AnimationUtils.loadAnimation(context, exitAnimation);
        exitAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(enterAnimation, 0)
                        .replace(containerId, newFragment)
                        .commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        currentFragment.getView().startAnimation(exitAnim);
    }
}
