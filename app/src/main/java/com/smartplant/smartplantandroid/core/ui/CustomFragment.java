package com.smartplant.smartplantandroid.core.ui;

import android.content.Context;
import android.view.View;

import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class CustomFragment extends Fragment {
    @Nullable
    public <T extends View> T findViewById(@IdRes int id) {
        View view;
        if ((view = this.getView()) == null) return null;
        return view.findViewById(id);
    }

    protected void _replaceFragment(@IdRes int fragmentId, @NonNull Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(fragmentId, fragment);
        fragmentTransaction.commit();
    }

    // With animation
    protected void _replaceFragment(@IdRes int fragmentId, @NonNull Fragment fragment, @AnimRes int enterAnim, @AnimRes int exitAnim, @AnimRes int popEnterAnim, @AnimRes int popExitAnim) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);

        fragmentTransaction.replace(fragmentId, fragment);
        fragmentTransaction.commit();
    }

    // With animation array
    protected void _replaceFragment(@IdRes int fragmentId, @NonNull Fragment fragment, @NonNull int[] animation) {
        if (animation.length != 4)
            throw new IllegalArgumentException("Animations array must have exactly 4 elements.");

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(animation[0], animation[1], animation[2], animation[3]);

        fragmentTransaction.replace(fragmentId, fragment);
        fragmentTransaction.commit();
    }

    // With full animation
    protected void _replaceFragmentWithFullAnimation(
            @Nullable Fragment currentFragment,
            @NonNull Fragment newFragment,
            @IdRes int containerId,
            @AnimRes int enterAnimation,
            @AnimRes int exitAnimation
    ) {
        Context context = getContext();
        if (context == null)
            throw new IllegalStateException("Unable to replace fragment: context is null");
        CustomAnimationUtils.replaceFragmentWithFullAnimation(context, getChildFragmentManager(), currentFragment, newFragment, containerId, enterAnimation, exitAnimation);
    }
}
