package ru.job4j.background_thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private Thread thread;
    private TextView threadText;
    private TextView info;
    private Bitmap bitmap;
    private ImageView image;
    private ProgressBar pb;
    private ProgressBar pb2;
    private ProgressBar pb3;
    private Disposable sbr;
    public static WeakReference<MainActivity> activityWeakReference;
    private String url = "https://cdn.trinixy.ru/pics6/20200224/189250_12_trinixy_ru.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton start_thread = findViewById(R.id.start_thread_button);
        ImageButton stop_thread = findViewById(R.id.stop_thread_button);
        threadText = findViewById(R.id.thread_count_textView);
        info = findViewById(R.id.info_textView);
        image = findViewById(R.id.imageView);
        Button asyncTaskButton = findViewById(R.id.async_task_button);
        Button startRx = findViewById(R.id.start_rx_button);
        Button stopRx = findViewById(R.id.stop_rx_button);
        pb = findViewById(R.id.progressBar);
        pb2 = findViewById(R.id.progressBar2);
        pb3 = findViewById(R.id.progressBar3);
        boolean recreated = savedInstanceState != null;
        final int startAt = recreated ? savedInstanceState.getInt(
                "pb3_progress", 0) : 0;
        String text = startAt + " %";
        info.setText(text);
        start_thread.setOnClickListener(v -> startThread());
        stop_thread.setOnClickListener(v -> stopThread());
        image.setOnClickListener(v -> new ImageAsyncTask(this).execute(url));
        asyncTaskButton.setOnClickListener(v ->
            new SampleAsyncTask(this).execute(20)
        );
        startRx.setOnClickListener(v -> startRx(startAt));
        stopRx.setOnClickListener(v -> stopRx());
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("picture", bitmap);
        outState.putString("text", threadText.getText().toString());
        outState.putInt("pb3_progress", pb3.getProgress());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bitmap = savedInstanceState.getParcelable("picture");
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
        }
        threadText.setText(savedInstanceState.getString("text"));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRx();
    }
    private void startThread() {
        thread = new Thread() {
            int count = 0;
            @Override
            public void run() {
                while (count != 30 && !isInterrupted()) {
                    ++count;
                    String text = getString(R.string.thread_count) + count;
                    Log.d("<<< THREAD TAG >>>", text);
                    runOnUiThread(() -> threadText.setText(text));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        thread.start();
        Toast.makeText(this, "Thread started.", Toast.LENGTH_SHORT).show();
    }
    private void stopThread() {
        if (thread != null) {
            if (thread.isAlive()) {
                Toast.makeText(this, "Thread stopped.", Toast.LENGTH_SHORT).show();
            }
            thread.interrupt();
        }
    }
    private Bitmap loadImageFromNetwork(String url) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
//=================================================================================================
    private static class SampleAsyncTask extends AsyncTask<Integer, Integer, String> {
        SampleAsyncTask(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }
        @Override
        protected String doInBackground(Integer... integers) {
            int count = 0;
            while (count < integers[0]) {
                publishProgress((count * 100) / integers[0]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
            }
            return "Finish";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity activity = activityWeakReference.get();
            activity.pb.setProgress(values[0]);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityWeakReference.get();
            if(activity == null || activity.isFinishing()) {
                return;
            }
            activity.pb.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MainActivity activity = activityWeakReference.get();
            Toast.makeText(activity.getBaseContext(), s, Toast.LENGTH_SHORT).show();
            activity.pb.setVisibility(View.INVISIBLE);
        }
    }
    private static class ImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {
        ImageAsyncTask(MainActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityWeakReference.get();
            if(activity == null || activity.isFinishing()) {
                return;
            }
            activity.pb2.setVisibility(View.VISIBLE);
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            MainActivity activity = activityWeakReference.get();
            int count = 0;
            Bitmap bitmap = null;
            while (count != 100) {
                publishProgress(count);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                bitmap = activity.loadImageFromNetwork(strings[0]);
            }
            publishProgress(count);
            return bitmap;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity activity = activityWeakReference.get();
            activity.pb2.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);
            MainActivity activity = activityWeakReference.get();
            activity.image.setImageBitmap(b);
            activity.bitmap = b;
            activity.pb2.setVisibility(View.INVISIBLE);
        }
    }
//=================================================================================================
    private void startRx(int startAt) {
        if (this.sbr == null || sbr.isDisposed()) {
            sbr = Observable.interval(1, TimeUnit.SECONDS).observeOn(
                    AndroidSchedulers.mainThread()).subscribe(
                    v -> {
                        info.setText(MessageFormat.format("{0}%", startAt + v.intValue()));
                        pb3.setVisibility(View.VISIBLE);
                        pb3.setProgress(startAt + v.intValue());
                        if (pb3.getProgress() == 100) {
                            stopRx();
                        }
                    });
        }
    }
    private void stopRx() {
        if (this.sbr != null) {
            sbr.dispose();
            this.pb3.setVisibility(View.INVISIBLE);
        }
    }
}
