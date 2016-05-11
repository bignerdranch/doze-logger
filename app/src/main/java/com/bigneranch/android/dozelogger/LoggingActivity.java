package com.bigneranch.android.dozelogger;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bigneranch.android.dozelogger.databinding.LogItemViewBinding;
import com.bigneranch.android.dozelogger.databinding.LoggingActivityBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LoggingActivity extends AppCompatActivity {

    private List<String> logs = new ArrayList<>();
    private LogAdapter logAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoggingActivityBinding loggingActivityBinding = DataBindingUtil.setContentView(this, R.layout.logging_activity);
        setupRecyclerView(loggingActivityBinding);
        setupOnClickListeners(loggingActivityBinding);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED);
        registerReceiver(new DeviceIdleReceiver(), filter);
    }

    private void setupRecyclerView(LoggingActivityBinding loggingActivityBinding) {
        loggingActivityBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        logs = PermanentLoggerUtil.getLogs(this);
        logAdapter = new LogAdapter();
        loggingActivityBinding.recyclerView.setAdapter(logAdapter);
    }

    private void setupOnClickListeners(LoggingActivityBinding loggingActivityBinding) {
        loggingActivityBinding.jobSchedulerButton.setOnClickListener(new OnClickListener() {
            @TargetApi(VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                jobScheduler.cancelAll();
                PermanentLoggerUtil.logMessage(LoggingActivity.this, "Cancelling all jobscheduler jobs");
                ComponentName componentName = new ComponentName(LoggingActivity.this, LoggingJobService.class);
                JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                        .setPeriodic(TimeUnit.MINUTES.toMillis(8))
                        .build();

                PermanentLoggerUtil.logMessage(LoggingActivity.this, "Scheduling recurring job");
                jobScheduler.schedule(jobInfo);
                refreshLogs();
            }
        });

        loggingActivityBinding.alarmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent = LoggingAlarmService.getNormalAlarmPendingIntent(LoggingActivity.this);
                PermanentLoggerUtil.logMessage(LoggingActivity.this, "Scheduling normal repeating Alarm");
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(),
                        AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                        pendingIntent);
                refreshLogs();
            }
        });

        loggingActivityBinding.idleAlarmButton.setOnClickListener(new OnClickListener() {
            @TargetApi(VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent = LoggingAlarmService.getIdleAlarmIntent(LoggingActivity.this);
                PermanentLoggerUtil.logMessage(LoggingActivity.this, "Scheduling exact and allow while idle Alarm");
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10),
                        pendingIntent);
                refreshLogs();
            }
        });

        loggingActivityBinding.cancelAllButton.setOnClickListener(new OnClickListener() {
            @TargetApi(VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                jobScheduler.cancelAll();
                PendingIntent pendingIntent = LoggingAlarmService.getNormalAlarmPendingIntent(LoggingActivity.this);
                pendingIntent.cancel();
                PermanentLoggerUtil.logMessage(LoggingActivity.this, "Cancelling all jobs");
                refreshLogs();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logging_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                refreshLogs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refreshLogs() {
        logs = PermanentLoggerUtil.getLogs(this);
        logAdapter.notifyDataSetChanged();
    }

    private class LogAdapter extends Adapter<LogViewHolder> {

        @Override
        public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LogItemViewBinding logItemViewBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.log_item_view, parent, false);
            return new LogViewHolder(logItemViewBinding);
        }

        @Override
        public void onBindViewHolder(LogViewHolder holder, int position) {
            holder.bind(logs.get(position));
        }

        @Override
        public int getItemCount() {
            return logs.size();
        }
    }


    private class LogViewHolder extends ViewHolder {
        private LogItemViewBinding logItemViewBinding;

        public LogViewHolder(LogItemViewBinding logItemViewBinding) {
            super(logItemViewBinding.getRoot());
            this.logItemViewBinding = logItemViewBinding;
        }

        public void bind(String text) {
            logItemViewBinding.textView.setText(text);
        }
    }
}
