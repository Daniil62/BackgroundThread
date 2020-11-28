package ru.job4j.background_thread;

import android.os.Handler;
import android.util.Log;

public class TestThread extends Thread {
    private int time;
    private int count = 0;
    TestThread(int time) {
        this.time = time;
    }
    @Override
    public void run() {
        Handler handler = MainActivity.getHandler();
        while (count != time && !isInterrupted()) {
            String TAG = "Runnable_tag";
            Log.d(TAG, "Thread count: " + count);
            ++count;
            handler.post(() -> MainActivity.setThreadCountText(count));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
