package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvTimer;
    private Button btnStart, btnStop, btnReset;
    private Handler handler = new Handler();
    private long startTime = 0L;
    private boolean isRunning = false;
    private long updateTime = 0L;
    private long timeBuffer = 0L;
    private long elapsedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTimer = findViewById(R.id.tvTimer);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnReset = findViewById(R.id.btnReset);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = System.currentTimeMillis();
                handler.postDelayed(runnable, 0);
                isRunning = true;
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                btnReset.setEnabled(true);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeBuffer += elapsedTime;
                handler.removeCallbacks(runnable);
                isRunning = false;
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0L;
                timeBuffer = 0L;
                updateTime = 0L;
                elapsedTime = 0L;
                tvTimer.setText("00:00:00");
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnReset.setEnabled(false);
            }
        });

        btnStop.setEnabled(false);
        btnReset.setEnabled(false);
    }

    private Runnable runnable = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTime = timeBuffer + elapsedTime;

            int secs = (int) (updateTime / 1000);
            int mins = secs / 60;
            int hours = mins / 60;
            secs = secs % 60;
            mins = mins % 60;

            tvTimer.setText(String.format("%02d:%02d:%02d", hours, mins, secs));
            handler.postDelayed(this, 1000);
        }
    };
}
