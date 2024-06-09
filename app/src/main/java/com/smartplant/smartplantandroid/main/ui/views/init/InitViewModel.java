package com.smartplant.smartplantandroid.main.ui.views.init;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.main.components.auth.repository.AuthRepositoryST;

public class InitViewModel extends ViewModel {
    private final MutableLiveData<Boolean> _isAuthenticatedLive = new MutableLiveData<>();
    private final AuthRepositoryST _authRepository;

    public InitViewModel() {
        this._authRepository = AuthRepositoryST.getInstance();
    }

    public LiveData<Boolean> getAuthenticatedLive() {
        return _isAuthenticatedLive;
    }

    public void checkAuthentication() {
        if (!this._authRepository.hasTokens()) this._isAuthenticatedLive.postValue(false);

        try {
            this._authRepository.fetchMe()
                    .onSuccess(((result, response, applicationResponse) -> {
                        AppLogger.debug("User authenticated");
                        this._isAuthenticatedLive.postValue(true);
                    }))
                    .onFailure((error -> {
                        AppLogger.debug("Unable to fetch user");
                        this._isAuthenticatedLive.postValue(false);
                        if (error instanceof UnauthorizedException) _authRepository.logout();
                    })).send();
        } catch (UnauthorizedException e) {
            this._isAuthenticatedLive.postValue(false);
        }
    }
}
