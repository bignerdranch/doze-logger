package com.bigneranch.android.dozelogger;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStreamWriter;

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
            logFileOutputStreamWriter.write(message);
            logFileOutputStreamWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "Failed to append line to file", e);
        }
    }

}
