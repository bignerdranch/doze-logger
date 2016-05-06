package com.bigneranch.android.dozelogger;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import com.bigneranch.android.dozelogger.databinding.LogItemViewBinding;
import com.bigneranch.android.dozelogger.databinding.LoggingActivityBinding;

import java.util.ArrayList;
import java.util.List;

public class LoggingActivity extends AppCompatActivity {

    private List<String> logs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoggingActivityBinding loggingActivityBinding = DataBindingUtil.setContentView(this, R.layout.logging_activity);
        loggingActivityBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        logs = PermanentLoggerUtil.getLogs(this);
        loggingActivityBinding.recyclerView.setAdapter(new LogAdapter());
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
