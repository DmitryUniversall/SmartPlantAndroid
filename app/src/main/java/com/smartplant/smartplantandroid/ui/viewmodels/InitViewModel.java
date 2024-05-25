package com.smartplant.smartplantandroid.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.utils.AppLogger;

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
                    .onSuccess(((result, response, transferResponse) -> {
                        AppLogger.debug("User authenticated");
                        this._isAuthenticatedLive.postValue(true);
                    }))
                    .onFailure(((call, error) -> {
                        AppLogger.debug("Unable to fetch user");
                        this._isAuthenticatedLive.postValue(false);
                    })).send();
        } catch (UnauthorizedException e) {
            this._isAuthenticatedLive.postValue(false);
        }
    }
}
