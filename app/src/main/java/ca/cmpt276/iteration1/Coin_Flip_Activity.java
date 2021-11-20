package ca.cmpt276.iteration1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
    Coin_Flip_Activity:
        - Includes a spinner to choose picker's name
        - Includes a radio group of heads/tails buttons for picker's guess
        - Press button to do a coin flip animation, with results randomized every time.
        - By default, child picks heads, and if the flip correspond to the child's guess
            a tick mark will show in coin flip history. Else, clear mark will show.
        - Sound is played upon flipping a coin, and coin stopped.
        - Result displayed as a text written heads or tails or clarification.

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

    private LocalDateTime time;
    private ImageView coin;
    private TextView info;
    private boolean cur_face; //true is head, false is tails.
    private int duration = 40;
    private int duration_increment = 0;
    private MediaPlayer player;
    private boolean guess;
    private String child_name;

    @Override
    protected void onResume() {
        super.onResume();
        setup_spin_coin_image_click();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setup_spin_coin_image_click();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_coin);

        setup_back_button();
        setup_params();
        setup_spin_coin_image_click();
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.coin_flip_back_button);
        back_button.setOnClickListener(view -> Coin_Flip_Activity.super.onBackPressed());
    }

    private void setup_params() {
        guess = getIntent().getBooleanExtra("GUESS",false);
        child_name = getIntent().getStringExtra("CHILD_NAME");
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
                        result.setText(R.string.heads_text);
                    else
                        result.setText(R.string.tails_text);
                    setup_Result(cur_face == guess);
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

            time = LocalDateTime.now();
            player = MediaPlayer.create(Coin_Flip_Activity.this,R.raw.coin_flip);
            player.start();
            info.setText("");
            coin.setEnabled(false);

            //Delay the flip animation by 240ms to match with audio
            final Handler handler = new Handler();
            handler.postDelayed(this::flip_animation, 240);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}