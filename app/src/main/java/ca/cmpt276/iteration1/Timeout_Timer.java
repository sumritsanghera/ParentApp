package ca.cmpt276.iteration1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import ca.cmpt276.iteration1.model.Timer_Message_Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Timeout_Timer extends AppCompatActivity {

    private EditText EditTextInput;
    private TextView TextViewCountdown;
    private RadioGroup radioGroup;
    private Button ButtonSet;
    private Button ButtonStartPause;
    private Button ButtonReset;

    private CountDownTimer CountDownTimer;

    private boolean isTimerRunning;

    private long StartTimeInMillis;
    private long TimeLeftInMillis;
    private long EndTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditTextInput = findViewById(R.id.edit_text_input);
        TextViewCountdown = findViewById(R.id.text_view_countdown);

        ButtonSet = findViewById(R.id.button_set);
        ButtonStartPause = findViewById(R.id.button_start_pause);
        ButtonReset = findViewById(R.id.button_reset);


        ButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = EditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(Timeout_Timer.this, "Field can't be empty",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(Timeout_Timer.this, "Please enter a positive number",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                EditTextInput.setText("");
            }
        });

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

        createRadioButtons();
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

                    long buttonTimer = numMinute * 60000;
                    setTime(buttonTimer);

                    Toast.makeText(Timeout_Timer.this, numMinute + " Minute Timer",
                            Toast.LENGTH_SHORT).show();
                }
            });
            radioGroup.addView(button);
        }
    }


    private void setTime(long milliseconds) {
        StartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }


    private void startTimer() {
        EndTime = System.currentTimeMillis() + TimeLeftInMillis;

        CountDownTimer = new CountDownTimer(TimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
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
        CountDownTimer.cancel();
        isTimerRunning = false;
        updateButtons();
        TextViewCountdown.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        TimeLeftInMillis = StartTimeInMillis;
        updateCountDownText();
        updateButtons();
    }

    private void updateCountDownText() {
        int hours = (int) (TimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((TimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (TimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        TextViewCountdown.setText(timeLeftFormatted);
    }

    private void updateButtons() {
        if (isTimerRunning) {

            TextViewCountdown.setVisibility(View.VISIBLE);
            EditTextInput.setVisibility(View.INVISIBLE);
            ButtonSet.setVisibility(View.INVISIBLE);
            ButtonReset.setVisibility(View.INVISIBLE);
            ButtonStartPause.setText("Pause");
            ButtonStartPause.setBackgroundColor(Color.GRAY);
            radioGroup.setVisibility(View.INVISIBLE);
        }
        else {
            //ButtonStartPause.setText("Start");
            ButtonStartPause.setBackgroundColor(Color.GREEN);
            ButtonStartPause.setText("Resume");
            EditTextInput.setVisibility(View.VISIBLE);
            ButtonSet.setVisibility(View.VISIBLE);
            TextViewCountdown.setVisibility(View.INVISIBLE);
            radioGroup.setVisibility(View.VISIBLE);

            //if timer is over
            if (TimeLeftInMillis < 1000) {
                ButtonStartPause.setVisibility(View.INVISIBLE);
                TextViewCountdown.setVisibility(View.VISIBLE);
                messageFragment();
            } else {
                ButtonStartPause.setVisibility(View.VISIBLE);
                ButtonStartPause.setText("Start");
            }


            if (TimeLeftInMillis < StartTimeInMillis) {
                ButtonReset.setVisibility(View.VISIBLE);
                ButtonReset.setBackgroundColor(Color.RED);
                radioGroup.setVisibility(View.VISIBLE);
            } else {
                ButtonReset.setVisibility(View.INVISIBLE);
                TextViewCountdown.setVisibility(View.VISIBLE);
            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", StartTimeInMillis);
        editor.putLong("millisLeft", TimeLeftInMillis);
        editor.putBoolean("timerRunning", isTimerRunning);
        editor.putLong("endTime", EndTime);

        editor.apply();

        if (CountDownTimer != null) {
            CountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        StartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        TimeLeftInMillis = prefs.getLong("millisLeft", StartTimeInMillis);
        isTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateButtons();

        if (isTimerRunning) {
            EndTime = prefs.getLong("endTime", 0);
            TimeLeftInMillis = EndTime - System.currentTimeMillis();

            if (TimeLeftInMillis < 0) {
                TimeLeftInMillis = 0;
                isTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }

    private void messageFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Timer_Message_Fragment dialog = new Timer_Message_Fragment();
        dialog.show(manager, "MessageDialog");

        Log.i("TAG", "Just Showed Dialog");

    }
}