package com.bigneranch.android.dozelogger;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION_CODES;
import android.os.PowerManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PermanentLoggerUtil {

    private static final String LOG_FILE = "DozeLog.txt";
    private static final String TAG = "DozeLogger";

    public static void logMessage(Context context, String message) {
        appendLine(context, message);
        Log.d(TAG, message);
    }

    synchronized private static void appendLine(Context context, String message) {
        try {
            OutputStreamWriter logFileOutputStreamWriter = new OutputStreamWriter(context.openFileOutput(LOG_FILE, Context.MODE_APPEND));
            String timeString = new SimpleDateFormat("HH:mm:ss").format(new Date());
            logFileOutputStreamWriter.write(timeString + ": " + message);
            logFileOutputStreamWriter.write('\n');
            logFileOutputStreamWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to append line to file", e);
        }
    }

    public static List<String> getLogs(Context context) {
        List<String> logs = new ArrayList<String>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(context.openFileInput(LOG_FILE)));
            String line = null;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
            }
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to read file", e);
        }

        return logs;
    }

    public static void logStatus(Context context) {
        logMessage(context, getStatus(context));
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    public static String getStatus(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean hasNetworkAccess = netInfo != null && netInfo.isConnectedOrConnecting();
        boolean isAvailable = netInfo != null && netInfo.isAvailable();

        String networkStatus = "Network connected: " + hasNetworkAccess + " Network available: " + isAvailable;



        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean partialWake = pm.isWakeLockLevelSupported(PowerManager.PARTIAL_WAKE_LOCK);
        String wakeLock = "Partial wakelock supported: " + partialWake;
//        boolean screenDimWake = pm.isWakeLockLevelSupported(PowerManager.SCREEN_DIM_WAKE_LOCK);
//        boolean screenBrightWake = pm.isWakeLockLevelSupported(PowerManager.SCREEN_BRIGHT_WAKE_LOCK);
//        boolean fullWake = pm.isWakeLockLevelSupported(PowerManager.FULL_WAKE_LOCK);

        return networkStatus + " " + wakeLock;
    }

}
