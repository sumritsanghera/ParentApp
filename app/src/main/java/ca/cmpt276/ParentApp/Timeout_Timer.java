package ca.cmpt276.ParentApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import ca.cmpt276.ParentApp.model.Timer_Message_Fragment;


import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

/*
    Timeout_Timer
    -   Displays Timer
    -   Able to Start/Pause/Reset Timer with buttons
    -   Includes Radio Buttons for Pre-set times, and able to input custom time
    -   Fragment + Alarm at end of Timer

    References:
        https://beginnersbook.com/2019/04/java-int-to-long-conversion/
        https://www.youtube.com/watch?v=MDuGwI6P-X8&list=PLrnPJCHvNZuB8wxqXCwKw2_NkyEmFwcSd&index=1
        https://www.youtube.com/watch?v=LMYQS1dqfo8&list=PLrnPJCHvNZuB8wxqXCwKw2_NkyEmFwcSd&index=2
        https://www.youtube.com/watch?v=lvibl8YJfGo&list=PLrnPJCHvNZuB8wxqXCwKw2_NkyEmFwcSd&index=3
        https://www.youtube.com/watch?v=7dQJAkjNEjM&list=PLrnPJCHvNZuB8wxqXCwKw2_NkyEmFwcSd&index=4
        https://www.youtube.com/watch?v=btk4229qI04 - videoBackground
        https://stackoverflow.com/questions/2618182/how-to-play-ringtone-alarm-sound-in-android
        https://stackoverflow.com/questions/26651602/display-back-arrow-on-toolbar
        https://stackoverflow.com/questions/50162052/working-example-of-countdown-timer-with-smooth-progress-bar
        https://www.youtube.com/watch?v=vYIw21_migM
        https://stackoverflow.com/questions/2131948/force-screen-on
 */

public class Timeout_Timer extends AppCompatActivity {


    private VideoView videoView;
    private EditText editTextInput;
    private TextView textViewCountDown;
    private RadioGroup radioGroup;
    private Button buttonSet;
    private Button buttonStartPause;
    private Button buttonReset;

    private CountDownTimer countDownTimer;

    private boolean isTimerRunning;
    private long startTimeInMillis;
    private long timeLeftInMillis = 1000;
    private long endTime;

    private ProgressBar progressBarCircle;
    private TextView textViewSpeed;
    private float timerSpeedMultiplier = 1;
    private final float defaultSpeed = 1;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_timer);

        videoView = findViewById(R.id.videoView);
        editTextInput = findViewById(R.id.edit_text_input);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        textViewSpeed = findViewById(R.id.text_view_speed);

        buttonSet = findViewById(R.id.button_set);
        buttonStartPause = findViewById(R.id.button_start_pause);
        buttonReset = findViewById(R.id.button_reset);
        progressBarCircle = findViewById(R.id.circular_progress_bar);
        toolbar = findViewById(R.id.toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        setup_toolbar();
        //setup_back_button();
        setUpSetButton();
        setUpStartPauseButton();
        setUpResetButton();
        createRadioButtons();
    }

    private void setup_toolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Timer");
        }
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.timer_back_button);
        back_button.setOnClickListener(view -> Timeout_Timer.super.onBackPressed());
    }


    private void setUpStartPauseButton() {
        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = textViewCountDown.getText().toString();

                if (input.equals("00:00")) {
                    Snackbar.make(v,R.string.enter_time_error,Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (isTimerRunning) {
                    pauseTimer();
                }
                else {
                    startTimer(defaultSpeed);
                }
            }
        });
    }

    private void setUpResetButton() {
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }


    private void setUpSetButton() {
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = editTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(Timeout_Timer.this, R.string.field_empty,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(Timeout_Timer.this, R.string.enter_positive_number,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisInput);
                editTextInput.setText("");
            }
        });
    }

    private void setTime(long milliseconds) {
        startTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }


    private void startTimer(final double timerSpeed) {
        endTime = System.currentTimeMillis() + timeLeftInMillis;

        countDownTimer = new CountDownTimer((long) (timeLeftInMillis / timerSpeed),
                (long) (1000 / timerSpeed)) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = (long) (millisUntilFinished * timerSpeed);
                //Set ProgressBar
                int progress = (int) timeLeftInMillis/1000;
                int maxProgress = (int) startTimeInMillis/1000;
                progressBarCircle.setMax(maxProgress);

                updateCountDownText();
                progressBarCircle.setProgress(progress);

            }
            @Override
            public void onFinish() {
                isTimerRunning = false;
                updateButtons();
            }
        }.start();

        isTimerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
        updateButtons();
        textViewCountDown.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        timeLeftInMillis = startTimeInMillis;
        updateCountDownText();
        updateButtons();
    }

    private void updateCountDownText() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        textViewCountDown.setText(timeLeftFormatted);
    }

    private void updateButtons() {
        if (isTimerRunning) {
            timerRunningActions();
        }
        else {
            timerPausedActions();

            //if timer is over
            if (timeLeftInMillis < 1000) {
                timerOverActions();
            }
            else {
                buttonStartPause.setVisibility(View.VISIBLE);
                buttonStartPause.setText(R.string.start);
            }

            if (timeLeftInMillis < startTimeInMillis) {
                buttonReset.setVisibility(View.VISIBLE);
                buttonReset.setBackgroundColor(Color.RED);
                radioGroup.setVisibility(View.VISIBLE);
            } else {
                videoView.setVisibility(View.INVISIBLE);
                progressBarCircle.setVisibility(View.INVISIBLE);
                buttonReset.setVisibility(View.INVISIBLE);
                textViewCountDown.setVisibility(View.VISIBLE);
                textViewCountDown.setTextColor(Color.BLACK);
            }
        }
    }

    private void timerOverActions() {
        progressBarCircle.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.INVISIBLE);
        buttonStartPause.setVisibility(View.INVISIBLE);
        textViewCountDown.setVisibility(View.VISIBLE);
        textViewCountDown.setTextColor(Color.BLACK);
        messageFragment();
        vibrate();
    }

    private void timerStartActions() {
        buttonStartPause.setBackgroundColor(Color.GREEN);
        buttonStartPause.setText(R.string.start);
        editTextInput.setVisibility(View.VISIBLE);
        buttonSet.setVisibility(View.VISIBLE);
        textViewCountDown.setVisibility(View.VISIBLE);
        textViewCountDown.setTextColor(Color.BLACK);
        radioGroup.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.INVISIBLE);

    }

    private void timerPausedActions() {
        buttonStartPause.setBackgroundColor(Color.GREEN);
        editTextInput.setVisibility(View.VISIBLE);
        buttonSet.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
        textViewCountDown.setVisibility(View.INVISIBLE);
    }

    private void timerRunningActions() {
        playVideo();
        progressBarCircle.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.VISIBLE);
        textViewCountDown.setVisibility(View.VISIBLE);
        editTextInput.setVisibility(View.INVISIBLE);
        buttonSet.setVisibility(View.INVISIBLE);
        buttonReset.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.INVISIBLE);
        buttonStartPause.setText(R.string.pause);
        textViewCountDown.setTextColor(Color.WHITE);
        buttonStartPause.setBackgroundColor(Color.GRAY);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //onStop and onStart methods for keeping timer running in background
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", startTimeInMillis);
        editor.putLong("millisLeft", timeLeftInMillis);
        editor.putBoolean("timerRunning", isTimerRunning);
        editor.putLong("endTime", endTime);
        editor.putFloat("timerSpeed", timerSpeedMultiplier);

        editor.apply();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        startTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        timeLeftInMillis = prefs.getLong("millisLeft", startTimeInMillis);
        isTimerRunning = prefs.getBoolean("timerRunning", false);
        timerSpeedMultiplier = prefs.getFloat("timerSpeed", 1);

        updateCountDownText();

        if (isTimerRunning) {
            endTime = prefs.getLong("endTime", 0);
            timeLeftInMillis = endTime - System.currentTimeMillis();

            if (timeLeftInMillis < 0) {
                timeLeftInMillis = 0;
                isTimerRunning = false;
                updateCountDownText();
                timerStartActions();
                textViewCountDown.setVisibility(View.VISIBLE);
            } else {
                startTimer(timerSpeedMultiplier);
            }
        }
    }

    private void messageFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Timer_Message_Fragment dialog = new Timer_Message_Fragment();
        dialog.show(manager, "MessageDialog");

        Log.i("TAG", "Just Showed Dialog");

    }

    private void createRadioButtons() {
        radioGroup = findViewById(R.id.radio_group_timer);
        int[] numMinutes = getResources().getIntArray(R.array.num_minutes);
        for(int i = 0; i < numMinutes.length; i++) {
            int numMinute = numMinutes[i];
            RadioButton button = new RadioButton(this);
            button.setText(numMinute + " Minutes");

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    long buttonTimer = (long) numMinute * 60000;
                    setTime(buttonTimer);

                    Snackbar.make(view,numMinute + " Minute Timer Selected",
                            Snackbar.LENGTH_LONG).show();
                }
            });
            radioGroup.addView(button);
        }
    }

    private void playVideo() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        videoView.setVideoURI(uri);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoView.start();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(1000,
                VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private void updateSpeed(float newSpeed) {
        pauseTimer();
        startTimer(newSpeed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timer_menu, menu);
        return true;
    }

    //timer speed buttons from toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        View view = findViewById(android.R.id.content);
        int timerSpeedText = 100;

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (isTimerRunning) {
            if (item.getItemId() == R.id.quarter_speed) {
                timerSpeedMultiplier = 0.25f;
                timerSpeedText = 25;
            }
            else if (item.getItemId() == R.id.half_speed) {
                timerSpeedMultiplier = 0.5f;
                timerSpeedText = 50;
            }
            else if (item.getItemId() == R.id.three_quarter_speed) {
                timerSpeedMultiplier = 0.75f;
                timerSpeedText = 75;
            }
            else if (item.getItemId() == R.id.normal_speed) {
                timerSpeedMultiplier = 1f;
                timerSpeedText = 100;
            }
            else if (item.getItemId() == R.id.double_speed) {
                timerSpeedMultiplier = 2f;
                timerSpeedText = 200;
            }
            else if (item.getItemId() == R.id.triple_speed) {
                timerSpeedMultiplier = 3f;
                timerSpeedText = 300;
            }
            else if (item.getItemId() == R.id.quadruple_speed) {
                timerSpeedMultiplier = 4f;
                timerSpeedText = 400;
            }
            textViewSpeed.setText("Time @"+timerSpeedText+"%");
            updateSpeed(timerSpeedMultiplier);
            saveTimerSpeed();
            return true;
        }
        else {
            Snackbar.make(view, R.string.start_timer_first,Snackbar.LENGTH_LONG ).show();
        }
        return true;
    }

    private void saveTimerSpeed() {
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("timerSpeed", timerSpeedMultiplier);
        editor.apply();
    }
}