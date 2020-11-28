package ru.job4j.background_thread;

import android.os.Handler;
import android.util.Log;

public class TestRunnable implements Runnable {
    private int time;
    private int count = 0;
    private boolean isRunning = true;
    boolean isRunning() {
        return isRunning;
    }
    TestRunnable(int time) {
        this.time = time;
    }
    @Override
    public void run() {
        Handler handler = MainActivity.getHandler();
        while (count != time && isRunning) {
            String TAG = "Runnable_tag";
            Log.d(TAG, "Runnable thread count: " + count);
            ++count;
            handler.post(() -> MainActivity.setRunnableCountText(count));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    void stop() {
        this.isRunning = false;
    }
}
