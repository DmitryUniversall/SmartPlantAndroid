package com.smartplant.smartplantandroid.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.utils.AppLogger;

public class InitViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isAuthenticated = new MutableLiveData<>();
    private final AuthRepositoryST authRepository;

    public InitViewModel() {
        this.authRepository = AuthRepositoryST.getInstance();
    }

    public LiveData<Boolean> getIsAuthenticated() {
        return isAuthenticated;
    }

    public void checkAuthentication() {
        if (!this.authRepository.hasTokens()) this.isAuthenticated.postValue(false);

        try {
            this.authRepository.fetchMe()
                    .onSuccess((result -> {
                        AppLogger.debug("User authenticated");
                        this.isAuthenticated.postValue(true);
                    }))
                    .onFailure(((call, error) -> {
                        AppLogger.debug("Unable to fetch user");
                        this.isAuthenticated.postValue(false);
                    })).send();
        } catch (UnauthorizedException e) {
            this.isAuthenticated.postValue(false);
        }
    }
}
