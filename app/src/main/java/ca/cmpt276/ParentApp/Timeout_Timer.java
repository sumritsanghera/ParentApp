package ca.cmpt276.ParentApp;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
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
//
    private boolean isTimerRunning;

    private long startTimeInMillis;
    private long timeLeftInMillis = 1000;
    private long endTime;
    private ProgressBar progressBarCircle;
    private double timerSpeedMultiplier = 1;
    private final double defaultSpeed = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_timer);

        videoView = findViewById(R.id.videoView);
        editTextInput = findViewById(R.id.edit_text_input);
        textViewCountDown = findViewById(R.id.text_view_countdown);

        buttonSet = findViewById(R.id.button_set);
        buttonStartPause = findViewById(R.id.button_start_pause);
        buttonReset = findViewById(R.id.button_reset);
        progressBarCircle = findViewById(R.id.circular_progress_bar);


        setup_back_button();
        setUpSetButton();
        setUpStartPauseButton();
        setUpResetButton();
        createRadioButtons();
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
                } else {
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
                //Set ProgressBar values
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
                textViewCountDown.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    private void timerOverActions() {
        videoView.setVisibility(View.INVISIBLE);
        buttonStartPause.setVisibility(View.INVISIBLE);
        textViewCountDown.setVisibility(View.VISIBLE);
        textViewCountDown.setTextColor(getResources().getColor(android.R.color.black));
        messageFragment();
        vibrate();
    }

    private void timerStartActions() {
        buttonStartPause.setBackgroundColor(Color.GREEN);
        buttonStartPause.setText(R.string.start);
        editTextInput.setVisibility(View.VISIBLE);
        buttonSet.setVisibility(View.VISIBLE);
        textViewCountDown.setVisibility(View.VISIBLE);
        textViewCountDown.setTextColor(getResources().getColor(android.R.color.black));
        radioGroup.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.INVISIBLE);

    }

    private void timerPausedActions() {
        buttonStartPause.setBackgroundColor(Color.GREEN);
        editTextInput.setVisibility(View.VISIBLE);
        buttonSet.setVisibility(View.VISIBLE);
        textViewCountDown.setVisibility(View.INVISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
    }

    private void timerRunningActions() {
        playVideo();
        progressBarCircle.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.VISIBLE);
        textViewCountDown.setTextColor(getResources().getColor(android.R.color.white));
        textViewCountDown.setVisibility(View.VISIBLE);
        editTextInput.setVisibility(View.INVISIBLE);
        buttonSet.setVisibility(View.INVISIBLE);
        buttonReset.setVisibility(View.INVISIBLE);
        buttonStartPause.setText(R.string.pause);
        buttonStartPause.setBackgroundColor(Color.GRAY);
        radioGroup.setVisibility(View.INVISIBLE);
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
                startTimer(defaultSpeed);
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
        // Vibrate for 500 milliseconds
        v.vibrate(VibrationEffect.createOneShot(1000,
                VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private void changeTimerSpeed(double newSpeed) {
        pauseTimer();
        startTimer(newSpeed);
    }
}