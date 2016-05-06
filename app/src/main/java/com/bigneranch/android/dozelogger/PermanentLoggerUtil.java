package com.bigneranch.android.dozelogger;

import android.content.Context;
import android.util.Log;

import com.google.common.base.Strings;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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
            logFileOutputStreamWriter.write(message);
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

}
