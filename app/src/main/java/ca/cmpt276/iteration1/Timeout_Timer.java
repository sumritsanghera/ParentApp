package ca.cmpt276.iteration1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ca.cmpt276.iteration1.R;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class Timeout_Timer extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 600000;
    private long TimeLeftInMillis = START_TIME_IN_MILLIS;

    private TextView TextViewCountdown;
    private Button ButtonStartPause;
    private Button ButtonReset;
    private CountDownTimer CountDownTimer;
    private boolean isTimerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_timeout_timer);
        TextViewCountdown = findViewById(R.id.text_view_countdown);
        ButtonStartPause = findViewById(R.id.button_start_pause);
        ButtonReset = findViewById(R.id.button_reset);

        ButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        ButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDownText();
    }
    private void startTimer() {
        CountDownTimer = new CountDownTimer(TimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                isTimerRunning = false;
                ButtonStartPause.setText("Start");
                ButtonStartPause.setVisibility(View.INVISIBLE);
                ButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();
        isTimerRunning = true;
        ButtonStartPause.setText("pause");
        ButtonReset.setVisibility(View.INVISIBLE);
    }
    private void pauseTimer() {
        CountDownTimer.cancel();
        isTimerRunning = false;
        ButtonStartPause.setText("Start");
        ButtonReset.setVisibility(View.VISIBLE);
    }
    private void resetTimer() {
        TimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        ButtonReset.setVisibility(View.INVISIBLE);
        ButtonStartPause.setVisibility(View.VISIBLE);
    }
    private void updateCountDownText() {
        int minutes = (int) (TimeLeftInMillis / 1000) / 60;
        int seconds = (int) (TimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        TextViewCountdown.setText(timeLeftFormatted);
    }

}