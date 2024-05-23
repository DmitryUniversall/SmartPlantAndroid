package com.smartplant.smartplantandroid.utils;

import android.util.Log;

import java.util.Locale;

public class AppLogger {
    private static String getTag(StackTraceElement element) {
        return String.format(Locale.US, "%s:%d", element.getFileName(), element.getLineNumber());
    }

    private static StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    // Methods for formatted messages
    public static void verbose(String format, Object... args) {
        StackTraceElement element = getCurrentStackTraceElement();
        Log.v(getTag(element), String.format(format, args));
    }

    public static void debug(String format, Object... args) {
        StackTraceElement element = getCurrentStackTraceElement();
        Log.d(getTag(element), String.format(format, args));
    }

    public static void info(String format, Object... args) {
        StackTraceElement element = getCurrentStackTraceElement();
        Log.i(getTag(element), String.format(format, args));
    }

    public static void warning(String format, Object... args) {
        StackTraceElement element = getCurrentStackTraceElement();
        Log.w(getTag(element), String.format(format, args));
    }

    public static void error(String format, Object... args) {
        StackTraceElement element = getCurrentStackTraceElement();
        Log.e(getTag(element), String.format(format, args));
    }

    public static void error(Throwable throwable, String format, Object... args) {
        StackTraceElement element = getCurrentStackTraceElement();
        Log.e(getTag(element), String.format(format, args), throwable);
    }

    // Methods for simple messages
    public static void verbose(String message) {
        StackTraceElement element = getCurrentStackTraceElement();
        Log.v(getTag(element), message);
    }

    public static void debug(String message) {
        StackTraceElement element = getCurrentStackTraceElement();
        Log.d(getTag(element), message);
    }

    public static void info(String message) {
        StackTraceElement element = getCurrentStackTraceElement();
        Log.i(getTag(element), message);
    }

    public static void warning(String message) {
        StackTraceElement element = getCurrentStackTraceElement();
        Log.w(getTag(element), message);
    }

    public static void error(String message) {
        StackTraceElement element = getCurrentStackTraceElement();
        Log.e(getTag(element), message);
    }

    public static void error(String message, Throwable throwable) {
        StackTraceElement element = getCurrentStackTraceElement();
        Log.e(getTag(element), message, throwable);
    }
}
