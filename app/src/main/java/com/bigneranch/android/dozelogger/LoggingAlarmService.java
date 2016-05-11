package com.bigneranch.android.dozelogger;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;

public class LoggingAlarmService extends IntentService {

    private static final String EXTRA_NORMAL_ALARM = "NormalAlarm";

    public LoggingAlarmService() {
        super("LoggingAlarmService");
    }

    public static PendingIntent getNormalAlarmPendingIntent(Context context) {
        Intent intent = new Intent(context, LoggingAlarmService.class);
        intent.putExtra(EXTRA_NORMAL_ALARM, true);

        return PendingIntent.getService(context,
                0, intent, 0);
    }

    public static PendingIntent getIdleAlarmIntent(Context context) {
        Intent intent = new Intent(context, LoggingAlarmService.class);
        intent.putExtra(EXTRA_NORMAL_ALARM, false);

        return PendingIntent.getService(context,
                1, intent, 0);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (intent.getBooleanExtra(EXTRA_NORMAL_ALARM, true)) {
                PermanentLoggerUtil.logMessage(this, "Normal Alarm Launched");
                PermanentLoggerUtil.logStatus(this);
            } else {
                PermanentLoggerUtil.logMessage(this, "Idle interrupting Alarm Launched");
                PermanentLoggerUtil.logStatus(this);
            }
        }
    }
}
