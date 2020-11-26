package ru.job4j.background_thread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TestThread thread;
    private TestRunnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton general_start = findViewById(R.id.general_start_button);
        ImageButton start_thread = findViewById(R.id.start_thread_button);
        ImageButton stop_thread = findViewById(R.id.stop_thread_button);
        ImageButton start_runnable = findViewById(R.id.start_runnable_thread_button);
        ImageButton stop_runnable = findViewById(R.id.stop_runnable_thread_button);
        ImageButton general_stop = findViewById(R.id.general_stop_button);
        general_start.setOnClickListener(this::generalStart);
        start_thread.setOnClickListener(this::startThread);
        stop_thread.setOnClickListener(this::stopThread);
        start_runnable.setOnClickListener(this::startRunnable);
        stop_runnable.setOnClickListener(this::stopRunnable);
        general_stop.setOnClickListener(this::generalStop);
    }
    public void generalStart(View view) {
        startThread();
        startRunnable();
    }
    private void startThread() {
        thread = new TestThread(13);
        Toast.makeText(this, "Thread started.", Toast.LENGTH_SHORT).show();
        thread.start();
    }
    public void startThread(View view) {
        startThread();
    }
    private void stopThread() {
        if (thread !=null) {
            if (thread.isRunning()) {
                Toast.makeText(this, "Thread stopped.", Toast.LENGTH_SHORT).show();
            }
            thread.stopThread();
        }
    }
    public void stopThread(View view) {
        stopThread();
    }
    private void startRunnable() {
        runnable = new TestRunnable(26);
        Toast.makeText(this, "runnable thread started.", Toast.LENGTH_SHORT).show();
        new Thread(runnable).start();
    }
    public void startRunnable(View view) {
        startRunnable();
    }
    private void stopRunnable() {
        if (runnable != null) {
            if (runnable.isRunning()) {
                Toast.makeText(this,
                        "Runnable thread stopped.", Toast.LENGTH_SHORT).show();
            }
            runnable.stop();
        }
    }
    public void stopRunnable(View view) {
        stopRunnable();
    }
    public void generalStop(View view) {
        stopThread();
        stopRunnable();
    }
}
