package com.smartplant.smartplantandroid.main.ui.views.auth;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.models.ApplicationResponse;
import com.smartplant.smartplantandroid.core.network.ApplicationStatusCodes;
import com.smartplant.smartplantandroid.main.components.auth.exceptions.AuthFailedException;
import com.smartplant.smartplantandroid.main.components.auth.repository.AuthRepositoryST;

public class AuthViewModel extends ViewModel {
    private final AuthRepositoryST _authRepository;

    public static class AuthResult {
        public final boolean ok;
        public final ApplicationStatusCodes.StatusCode statusCode;

        public AuthResult(boolean ok, ApplicationStatusCodes.StatusCode statusCode) {
            this.ok = ok;
            this.statusCode = statusCode;
        }
    }

    public AuthViewModel() {
        this._authRepository = AuthRepositoryST.getInstance();
    }

    private void handleAuthSuccess(ApplicationResponse applicationResponse, MediatorLiveData<AuthResult> authResult) {
        AuthResult result = new AuthResult(true, applicationResponse.getStatusCode());
        authResult.postValue(result);
    }

    private void handleAuthFailure(Throwable error, MediatorLiveData<AuthResult> authResult) {
        ApplicationResponse applicationResponse;
        if (error instanceof AuthFailedException && (applicationResponse = ((AuthFailedException) error).getApplicationResponse()) != null) {
            AuthResult result = new AuthResult(false, applicationResponse.getStatusCode());
            authResult.postValue(result);
        } else {
            AuthResult result = new AuthResult(false, ApplicationStatusCodes.GENERIC_ERRORS.INTERNAL_SERVER_ERROR);
            authResult.postValue(result);
        }
    }

    public MediatorLiveData<AuthResult> sendRegister(String username, String password) {
        MediatorLiveData<AuthResult> registerAuthResult = new MediatorLiveData<>();

        this._authRepository.register(username, password)
                .onSuccess(((result, response, applicationResponse) -> {
                    AppLogger.debug("Register successful");
                    this.handleAuthSuccess(applicationResponse, registerAuthResult);
                }))
                .onFailure(((call, error) -> {
                    AppLogger.error("Register unsuccessful", error);

                    this.handleAuthFailure(error, registerAuthResult);
                })).send();

        return registerAuthResult;
    }

    public MediatorLiveData<AuthResult> sendLogin(String username, String password) {
        MediatorLiveData<AuthResult> loginAuthResult = new MediatorLiveData<>();

        this._authRepository.login(username, password)
                .onSuccess(((result, response, applicationResponse) -> {
                    AppLogger.debug("Login successful");
                    this.handleAuthSuccess(applicationResponse, loginAuthResult);
                }))
                .onFailure(((call, error) -> {
                    AppLogger.error("Login unsuccessful", error);

                    this.handleAuthFailure(error, loginAuthResult);
                })).send();

        return loginAuthResult;
    }
}
