package com.smartplant.smartplantandroid.core.ui;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CustomFragment extends Fragment {
    @Nullable
    public <T extends View> T findViewById(@IdRes int id) {
        View view;
        if ((view = this.getView()) == null) return null;
        return view.findViewById(id);
    }
}
