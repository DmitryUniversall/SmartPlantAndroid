package com.smartplant.smartplantandroid.ui.viewmodels;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.R;
import com.smartplant.smartplantandroid.auth.exceptions.AuthFailedException;
import com.smartplant.smartplantandroid.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.network.ApplicationResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuthViewModel extends ViewModel {
    private final AuthRepositoryST _authRepository;
    private static final Map<Integer, Integer> APPLICATION_STATUS_CODE_MAPPING;

    static {
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(1001, R.string.success);
        map.put(2000, R.string.internalServerError);
        map.put(2001, R.string.userAlreadyExist);  // Register only
        map.put(3006, R.string.wrongAuthCredentials);  // Login only

        APPLICATION_STATUS_CODE_MAPPING = Collections.unmodifiableMap(map);
    }

    public static class AuthResult {
        public final boolean ok;
        public final int applicationStatusCode;
        public final int message;

        public AuthResult(boolean ok, int applicationStatusCode, int message) {
            this.ok = ok;
            this.applicationStatusCode = applicationStatusCode;
            this.message = message;
        }
    }

    public AuthViewModel() {
        this._authRepository = AuthRepositoryST.getInstance();
    }

    private void handleAuthSuccess(ApplicationResponse applicationResponse, MediatorLiveData<AuthResult> authResult) {
        int applicationStatusCode = applicationResponse.getApplicationStatusCode();

        Integer message = APPLICATION_STATUS_CODE_MAPPING.getOrDefault(applicationStatusCode, R.string.success);
        assert message != null;

        authResult.postValue(new AuthResult(true, applicationStatusCode, message));
    }

    private void handleAuthFailure(Throwable error, MediatorLiveData<AuthResult> authResult) {
        ApplicationResponse applicationResponse;
        if (error instanceof AuthFailedException && (applicationResponse = ((AuthFailedException) error).getTransferResponse()) != null) {
            int applicationStatusCode = applicationResponse.getApplicationStatusCode();

            Integer message = APPLICATION_STATUS_CODE_MAPPING.get(applicationStatusCode);
            assert message != null;

            authResult.postValue(new AuthResult(false, applicationStatusCode, message));
        } else {
            Integer message = APPLICATION_STATUS_CODE_MAPPING.get(2000);
            assert message != null;

            authResult.postValue(new AuthResult(false, -1, message));
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
