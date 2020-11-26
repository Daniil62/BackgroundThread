package ru.job4j.background_thread;

import android.util.Log;

public class TestThread extends Thread {
    private int time;
    private int count = 0;
    private boolean isRunning = true;
    boolean isRunning() {
        return isRunning;
    }
    TestThread(int time) {
        this.time = time;
    }
    @Override
    public void run() {

        while (count != time && isRunning) {
            String TAG = "Runnable_tag";
            Log.d(TAG, "Thread count: " + count);
            ++count;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    void stopThread() {
        this.isRunning = false;
    }
}
