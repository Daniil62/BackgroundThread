package ru.job4j.background_thread;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TestThread thread;
    private TestRunnable runnable;
    private static Handler handler;
    @SuppressLint("StaticFieldLeak")
    private static TextView threadText;
    @SuppressLint("StaticFieldLeak")
    private static TextView runnableText;
    private ImageView image;
    private String url = "https://fb.ru/misc/i/gallery/11421/163079.jpg";
    public static Handler getHandler() {
        return handler;
    }
    @SuppressLint("SetTextI18n")
    public static void setThreadCountText(int time) {
        threadText.setText("Thread " + time);
    }
    @SuppressLint("SetTextI18n")
    public static void setRunnableCountText(int time) {
        runnableText.setText("Runnable " + time);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton general_start = findViewById(R.id.general_start_button);
        ImageButton start_thread = findViewById(R.id.start_thread_button);
        ImageButton stop_thread = findViewById(R.id.stop_thread_button);
        threadText = findViewById(R.id.thread_count_textView);
        ImageButton start_runnable = findViewById(R.id.start_runnable_thread_button);
        ImageButton stop_runnable = findViewById(R.id.stop_runnable_thread_button);
        ImageButton general_stop = findViewById(R.id.general_stop_button);
        runnableText = findViewById(R.id.runnable_count_textView);
        this.image = findViewById(R.id.imageView);
        handler = new Handler();
        general_start.setOnClickListener(this::generalStart);
        start_thread.setOnClickListener(this::startThread);
        stop_thread.setOnClickListener(this::stopThread);
        start_runnable.setOnClickListener(this::startRunnable);
        stop_runnable.setOnClickListener(this::stopRunnable);
        general_stop.setOnClickListener(this::generalStop);
        image.setOnClickListener(v -> {
            Bitmap bitmap = loadImageFromNetWork(url);
            if (bitmap != null) {
                image.setImageBitmap(bitmap);
            }
        });
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
        if (thread != null) {
            if (thread.isAlive()) {
                Toast.makeText(this, "Thread stopped.", Toast.LENGTH_SHORT).show();
            }
            thread.interrupt();
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
    private Bitmap loadImageFromNetWork(String url) {
        Bitmap result = null;
        try {
            result = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }
}
