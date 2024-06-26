package com.smartplant.smartplantandroid.core.network;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ApplicationStatusCodes {
    private static final Map<Integer, StatusCode> _statusCodeMap = new HashMap<>();

    public static void initialize(Context context) {
        GENERICS.init(context);
        GENERIC_ERRORS.init(context);
        AUTH.init(context);
        DEVICES.init(context);
        STORAGE.init(context);
    }

    private static String getStringResource(Context context, int resId) {
        return context.getString(resId);
    }

    public static class StatusCode {
        private final int _code;
        private final @NonNull String _message;
        private final @NonNull String _translatedMessage;

        public StatusCode(int code, @NonNull String message, @NotNull String translatedMessage) {
            this._code = code;
            this._message = message;
            this._translatedMessage = translatedMessage;
        }

        @NonNull
        @Override
        public String toString() {
            return String.format(Locale.getDefault(), "StatusCode[%d; %s]", this._code, this._message);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            return obj instanceof StatusCode && this._code == ((StatusCode) obj).getCode();
        }

        public int getCode() {
            return _code;
        }

        public @NonNull String getMessage() {
            return _message;
        }

        public @NonNull String getTranslatedMessage() {
            return this._translatedMessage;
        }
    }

    public static @Nullable StatusCode findStatusCodeByCode(int code) {
        return _statusCodeMap.get(code);
    }

    public static @NonNull StatusCode findStatusCodeByCode(int code, @Nullable StatusCode def) {
        return Objects.requireNonNull(_statusCodeMap.getOrDefault(code, def != null ? def : GENERICS.NOT_SPECIFIED));
    }

    public static class GENERICS {
        public static StatusCode NOT_SPECIFIED;
        public static StatusCode SUCCESS;
        public static StatusCode CREATED;
        public static StatusCode UPDATED;
        public static StatusCode NOT_CHANGED;

        public static void init(Context context) {
            NOT_SPECIFIED = new StatusCode(0, "Not specified", getStringResource(context, R.string.code_not_specified));
            SUCCESS = new StatusCode(1000, "Success", getStringResource(context, R.string.code_success));
            CREATED = new StatusCode(1001, "Created", getStringResource(context, R.string.code_created));
            UPDATED = new StatusCode(1002, "Updated", getStringResource(context, R.string.code_updated));
            NOT_CHANGED = new StatusCode(1003, "Not changed", getStringResource(context, R.string.code_not_changed));

            _statusCodeMap.put(NOT_SPECIFIED.getCode(), NOT_SPECIFIED);
            _statusCodeMap.put(SUCCESS.getCode(), SUCCESS);
            _statusCodeMap.put(CREATED.getCode(), CREATED);
            _statusCodeMap.put(UPDATED.getCode(), UPDATED);
            _statusCodeMap.put(NOT_CHANGED.getCode(), NOT_CHANGED);
        }
    }

    public static class GENERIC_ERRORS {
        public static StatusCode INTERNAL_SERVER_ERROR;
        public static StatusCode ALREADY_EXISTS;
        public static StatusCode NOT_FOUND;
        public static StatusCode UNPROCESSABLE_ENTITY;
        public static StatusCode TIME_OUT;
        public static StatusCode BAD_REQUEST;
        public static StatusCode FORBIDDEN;

        public static void init(Context context) {
            INTERNAL_SERVER_ERROR = new StatusCode(2000, "Internal server error", getStringResource(context, R.string.code_internal_server_error));
            ALREADY_EXISTS = new StatusCode(2001, "Already exists", getStringResource(context, R.string.code_already_exists));
            NOT_FOUND = new StatusCode(2002, "Not found", getStringResource(context, R.string.code_not_found));
            UNPROCESSABLE_ENTITY = new StatusCode(2003, "Unprocessable entity", getStringResource(context, R.string.code_unprocessable_entity));
            TIME_OUT = new StatusCode(2004, "Time out", getStringResource(context, R.string.code_time_out));
            BAD_REQUEST = new StatusCode(2005, "Bad request", getStringResource(context, R.string.code_bad_request));
            FORBIDDEN = new StatusCode(2006, "Forbidden", getStringResource(context, R.string.code_forbidden));

            _statusCodeMap.put(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR);
            _statusCodeMap.put(ALREADY_EXISTS.getCode(), ALREADY_EXISTS);
            _statusCodeMap.put(NOT_FOUND.getCode(), NOT_FOUND);
            _statusCodeMap.put(UNPROCESSABLE_ENTITY.getCode(), UNPROCESSABLE_ENTITY);
            _statusCodeMap.put(TIME_OUT.getCode(), TIME_OUT);
            _statusCodeMap.put(BAD_REQUEST.getCode(), BAD_REQUEST);
            _statusCodeMap.put(FORBIDDEN.getCode(), FORBIDDEN);
        }
    }

    public static class AUTH {
        public static StatusCode AUTHORIZATION_NOT_SPECIFIED;
        public static StatusCode AUTHORIZATION_INVALID;
        public static StatusCode AUTHORIZATION_TYPE_UNKNOWN;
        public static StatusCode TOKEN_NOT_SPECIFIED;
        public static StatusCode TOKEN_EXPIRED;
        public static StatusCode TOKEN_INVALID;
        public static StatusCode TOKEN_VALIDATION_FAILED;
        public static StatusCode UNKNOWN_USER;
        public static StatusCode WRONG_AUTH_CREDENTIALS;

        public static void init(Context context) {
            AUTHORIZATION_NOT_SPECIFIED = new StatusCode(3001, "Authorization was not specified", getStringResource(context, R.string.code_authorization_not_specified));
            AUTHORIZATION_INVALID = new StatusCode(3002, "Invalid authorization", getStringResource(context, R.string.code_authorization_invalid));
            AUTHORIZATION_TYPE_UNKNOWN = new StatusCode(3003, "Unknown authorization type", getStringResource(context, R.string.code_authorization_type_unknown));
            TOKEN_NOT_SPECIFIED = new StatusCode(3004, "Authorization token was not specified", getStringResource(context, R.string.code_token_not_specified));
            TOKEN_EXPIRED = new StatusCode(3005, "Authorization token is expired", getStringResource(context, R.string.code_token_expired));
            TOKEN_INVALID = new StatusCode(3006, "Authorization token is invalid", getStringResource(context, R.string.code_token_invalid));
            TOKEN_VALIDATION_FAILED = new StatusCode(3007, "Authorization token validation failed", getStringResource(context, R.string.code_token_validation_failed));
            UNKNOWN_USER = new StatusCode(3008, "Invalid authorization token payload: User not found", getStringResource(context, R.string.code_unknown_user));
            WRONG_AUTH_CREDENTIALS = new StatusCode(3009, "Wrong authentication credentials", getStringResource(context, R.string.code_wrong_auth_credentials));

            _statusCodeMap.put(AUTHORIZATION_NOT_SPECIFIED.getCode(), AUTHORIZATION_NOT_SPECIFIED);
            _statusCodeMap.put(AUTHORIZATION_INVALID.getCode(), AUTHORIZATION_INVALID);
            _statusCodeMap.put(AUTHORIZATION_TYPE_UNKNOWN.getCode(), AUTHORIZATION_TYPE_UNKNOWN);
            _statusCodeMap.put(TOKEN_NOT_SPECIFIED.getCode(), TOKEN_NOT_SPECIFIED);
            _statusCodeMap.put(TOKEN_EXPIRED.getCode(), TOKEN_EXPIRED);
            _statusCodeMap.put(TOKEN_INVALID.getCode(), TOKEN_INVALID);
            _statusCodeMap.put(TOKEN_VALIDATION_FAILED.getCode(), TOKEN_VALIDATION_FAILED);
            _statusCodeMap.put(UNKNOWN_USER.getCode(), UNKNOWN_USER);
            _statusCodeMap.put(WRONG_AUTH_CREDENTIALS.getCode(), WRONG_AUTH_CREDENTIALS);
        }
    }

    public static class DEVICES {
        public static StatusCode INVALID_USER_OR_DEVICE;
        public static StatusCode ALREADY_HAS_OWNER;
        public static StatusCode CROSS_NETWORK_REQUEST;
        public static StatusCode PAIR_REQUEST_ACCEPTED;
        public static StatusCode PAIR_REQUEST_REJECTED;

        public static void init(Context context) {
            INVALID_USER_OR_DEVICE = new StatusCode(4001, "Invalid user or device", getStringResource(context, R.string.code_invalid_user_or_device));
            ALREADY_HAS_OWNER = new StatusCode(4002, "This device already has owner", getStringResource(context, R.string.code_already_has_owner));
            CROSS_NETWORK_REQUEST = new StatusCode(4003, "Cross-Network requests are not allowed", getStringResource(context, R.string.code_cross_network_request));
            PAIR_REQUEST_ACCEPTED = new StatusCode(4401, "Pair request accepted", getStringResource(context, R.string.code_pair_request_accepted));
            PAIR_REQUEST_REJECTED = new StatusCode(4402, "Pair request rejected", getStringResource(context, R.string.code_pair_request_rejected));

            _statusCodeMap.put(INVALID_USER_OR_DEVICE.getCode(), INVALID_USER_OR_DEVICE);
            _statusCodeMap.put(ALREADY_HAS_OWNER.getCode(), ALREADY_HAS_OWNER);
            _statusCodeMap.put(CROSS_NETWORK_REQUEST.getCode(), CROSS_NETWORK_REQUEST);
            _statusCodeMap.put(PAIR_REQUEST_ACCEPTED.getCode(), PAIR_REQUEST_ACCEPTED);
            _statusCodeMap.put(PAIR_REQUEST_REJECTED.getCode(), PAIR_REQUEST_REJECTED);
        }
    }

    public static class STORAGE {
        public static StatusCode INVALID_STORAGE_REQUEST;
        public static StatusCode INVALID_RESPONSE_DATA_MESSAGE;

        public static void init(Context context) {
            INVALID_STORAGE_REQUEST = new StatusCode(4003, "Invalid storage request", getStringResource(context, R.string.code_invalid_storage_request));
            INVALID_RESPONSE_DATA_MESSAGE = new StatusCode(4004, "Response-StorageDataMessage must be valid ApplicationResponsePayload", getStringResource(context, R.string.code_invalid_response_data_message));

            _statusCodeMap.put(INVALID_STORAGE_REQUEST.getCode(), INVALID_STORAGE_REQUEST);
            _statusCodeMap.put(INVALID_RESPONSE_DATA_MESSAGE.getCode(), INVALID_RESPONSE_DATA_MESSAGE);
        }
    }
}
