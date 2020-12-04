package ru.job4j.background_thread;

import android.util.Log;

public class TestThread extends Thread {
    @Override
    public void run() {
        int count = 0;
        while (!isInterrupted()) {
            String TAG = "Runnable_tag";
            Log.d(TAG, "Thread count: " + count);
            ++count;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
