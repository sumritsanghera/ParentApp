package ca.cmpt276.iteration1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/*
Description:
    Press button to do a coin flip animation, with results randomized every time.
    By default, child picks heads, and if the flip correspond to the child's guess
        a tick mark will show. Else, clear mark will show.
    Sound is played upon flipping a coin, and coin stopped.


References:
https://stackoverflow.com/questions/7785649/creating-a-3d-flip-animation-in-android-using-xml
https://stackoverflow.com/questions/46048176/how-to-start-animatorset-consecutively
https://stackoverflow.com/questions/6355495/animation-with-animationset-in-android
https://stackoverflow.com/questions/37323110/how-to-use-delay-functions-in-android-studio/37323343
Coin flip sound:
https://freesound.org/people/InspectorJ/sounds/342242/
https://stackoverflow.com/questions/3072173/how-to-call-a-method-after-a-delay-in-android
 */

public class Coin_Flip_Activity extends AppCompatActivity {


    private ArrayList<String> children_list = new ArrayList<>();
    private Spinner spinner;
    private static final String LAST_PICKER = "Last Picker";
    private static final String PREFS_NAME = "Coin_flip_prefs";
    private static final String CHILD_GUESS = "Child's guess";

    private LocalDateTime time;
    private ImageView coin;
    private TextView info;
    private boolean cur_face; //true is head, false is tails.
    private int duration = 40;
    private int duration_increment = 0;
    private MediaPlayer player;
    private boolean guess;
    private RadioGroup options;
    private RadioButton heads;
    private RadioButton tails;
    private String child_name;

    @Override
    protected void onResume() {
        super.onResume();
        refresh_option();
        setup_spin_coin_image_click();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh_option();
        setup_spin_coin_image_click();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);

        setup_back_button();
        setup_children_list();
        setup_child_option_spinner();
        setup_radioButton_listener();

        refresh_option();
        setup_spin_coin_image_click();
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.coin_flip_back_button);
        back_button.setOnClickListener(view -> Coin_Flip_Activity.super.onBackPressed());
    }

    //INPUT: Arraylist of strings of names
    private void setup_children_list() {
        Intent data = getIntent();
        children_list = data.getStringArrayListExtra("CHILDREN_LIST");
        children_list.add("No name");
    }

    private void setup_child_option_spinner() {
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Coin_Flip_Activity.this,
                R.layout.spinner_list,
                children_list
        );
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String picker = (String) adapterView.getItemAtPosition(i);

                child_name = picker;

                save_last_picker(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void save_last_picker(int i) {
        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(LAST_PICKER, i);
        editor.apply();
    }

    private void setup_radioButton_listener() {
        heads = findViewById(R.id.radioHeads);
        heads.setOnClickListener(view -> { saveRadioOptions(true);});
        tails = findViewById(R.id.radioTails);
        tails.setOnClickListener(view -> { saveRadioOptions(false);});

    }

    private void saveRadioOptions(boolean radio_option){
        SharedPreferences prefs = this.getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        //1 is Heads, 2 is Tails
        if(radio_option){
            editor.putBoolean(CHILD_GUESS, true);
        } else{
            editor.putBoolean(CHILD_GUESS, false);
        }
        editor.apply();

    }

    public static int getLastPicker(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        return prefs.getInt(LAST_PICKER,0);
    }

    public static boolean getOption(Context context){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        return prefs.getBoolean(CHILD_GUESS,false);
    }

    private void refresh_option() {
        spinner.setSelection(getLastPicker(this));
        guess = Coin_Flip_Activity.getOption(this);
        Log.e("Coin_Flip.java", "get option is: " + Coin_Flip_Activity.getOption(this));
    }

    /*
        Coin flip function:
        Create two animations to flip:
            - first one flips from 0 to 90 degrees
            - second one flips from 270 to 360 degrees
        In between of flips:
            - set image of coin to be the opposite side of current coin face.
            - Increment duration of flipping animation to decelerate the coin flip,
                to make animation look more natural.
        Play animation sequentially using animator set:
            - set startDelay to match animation with sound effect
            - add a listener to do repetitions (10 flips if randomizer is heads, else 11 flips
                if randomizer is tails)
            - generate random result (heads or tails) in the first flip
         After the last flip, stop calling animator set:
            - set button to be enabled
            - set textview to show result
            - show tick mark or false mark
     */
    private void flip_animation() {

        ObjectAnimator anim = ObjectAnimator.ofFloat(coin, "rotationX", 0f, 90f);
        anim.setDuration(duration);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(cur_face){
                    cur_face = false;
                    coin.setImageResource(R.drawable.tails);
                }else{
                    cur_face = true;
                    coin.setImageResource(R.drawable.heads);
                }
                duration += duration_increment;
                anim.setDuration(duration);

            }
        });
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(coin, "rotationX", 270f, 360f);
        anim2.setDuration(duration);
        anim2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                duration += duration_increment;
                anim2.setDuration(duration);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(anim,anim2);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {

            int count = 0;
            int COUNT = 10;

            @Override
            public void onAnimationEnd(Animator animation) {
                if (count == 0){
                    if(Math.random() > 0.5)
                        COUNT = 8;
                    else
                        COUNT = 7;
                    if(COUNT == 8){
                        duration = 30;
                    }
                    count++;
                    set.start();
                } else if ((count == 4) && (COUNT == 7)){
                    duration_increment = 80;
                    count++;
                    set.start();
                }else if ((count == 5) && (COUNT == 8)) {
                    duration_increment = 80;
                    count++;
                    set.start();
                }
                else if(count!= (COUNT-1)){
                    count++;
                    set.start();
                } else {
                    TextView result = findViewById(R.id.result);
                    duration = 40;
                    duration_increment = 0;
                    if(cur_face)
                        result.setText("Heads");
                    else
                        result.setText("Tails");
                    if(cur_face == guess) {
                        setup_Result(true);
                    }
                    else {
                        setup_Result(false);
                    }
                }
            }
        });

    }

    private void setup_Result(Boolean result) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd - HH:mma");

        Intent intent = new Intent();
        intent.putExtra("PICKER", child_name);
        intent.putExtra("RESULT",result);
        intent.putExtra("TIME", time.format(format));
        setResult(RESULT_OK,intent);
    }

    private void setup_spin_coin_image_click() {
        cur_face = true;
        coin = findViewById(R.id.coin_img_view);
        info = findViewById(R.id.coin_info);
        coin.setSoundEffectsEnabled(false);
        coin.setOnClickListener(view -> {
            options = findViewById(R.id.radioGroup);
            if (options.getCheckedRadioButtonId() == -1)
            {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                Snackbar.make(view,"Select Heads or Tails!",Snackbar.LENGTH_LONG).show();
            } else {
                time = LocalDateTime.now();
                guess = heads.isChecked();
                player = MediaPlayer.create(Coin_Flip_Activity.this,R.raw.coin_flip);
                player.start();
                info.setText("");
                coin.setEnabled(false);
                spinner.setEnabled(false);
                heads.setEnabled(false);
                tails.setEnabled(false);

                //Delay the flip animation by 240ms to match with audio
                final Handler handler = new Handler();
                handler.postDelayed(() -> flip_animation(), 240);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}