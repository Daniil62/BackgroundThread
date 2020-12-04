package ru.job4j.background_thread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Thread thread;
    private TextView threadText;
    private Bitmap bitmap;
    private ImageView image;
    private String url = "https://cdn.trinixy.ru/pics6/20200224/189250_12_trinixy_ru.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton start_thread = findViewById(R.id.start_thread_button);
        ImageButton stop_thread = findViewById(R.id.stop_thread_button);
        threadText = findViewById(R.id.thread_count_textView);
        image = findViewById(R.id.imageView);
        start_thread.setOnClickListener(v -> startThread());
        stop_thread.setOnClickListener(v -> stopThread());
        image.setOnClickListener(v -> new Thread(() -> {
            bitmap = loadImageFromNetwork(url);
            if (bitmap != null) {
                image.post(() -> image.setImageBitmap(bitmap));
            }
        }).start());
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("picture", bitmap);
        outState.putString("text", threadText.getText().toString());
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
}
