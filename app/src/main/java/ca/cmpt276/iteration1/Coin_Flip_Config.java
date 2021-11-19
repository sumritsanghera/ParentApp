package ca.cmpt276.iteration1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import ca.cmpt276.iteration1.model.Child;

public class Coin_Flip_Config extends AppCompatActivity {

    private ArrayList<Child> children_list = new ArrayList<>();
    private ActivityResultLauncher<Intent> picker_launcher;
    private ActivityResultLauncher<Intent> coin_flip_launcher;
    private TextView picker_name;
    private RadioGroup options;
    private Boolean guess; //true is heads, false is tails.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_config);

        setup_children_list();
        setup_default_picker();

        setup_choose_picker_launcher();
        setup_coin_flip_launcher();

        setup_back_button();
        setup_choose_picker();
        setup_radio_group();
        setup_flip_coin();
    }



    private void setup_children_list() {
        Intent data = getIntent();
        children_list = data.getParcelableArrayListExtra("CHILDREN_LIST");
        Child no_name = new Child("No name");
        children_list.add(no_name);
    }
    private void setup_default_picker() {
        picker_name = findViewById(R.id.picker_name);
        picker_name.setText(children_list.get(0).getName());
    }

    private void setup_choose_picker_launcher() {
        picker_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        int index = data.getIntExtra("CHILD_INDEX",0);
                        picker_name = findViewById(R.id.picker_name);
                        picker_name.setText(children_list.get(index).getName());
                    }
                }
        );
    }

    private void setup_coin_flip_launcher() {
        coin_flip_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Intent intent = new Intent();
                        intent.putExtra("PICKER", data.getStringExtra("PICKER"));
                        intent.putExtra("RESULT",data.getBooleanExtra("RESULT",false));
                        intent.putExtra("TIME", data.getStringExtra("TIME"));
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }
        );
    }

    private void setup_back_button() {
        ImageView button = findViewById(R.id.coin_config_back_button);
        button.setOnClickListener(view -> Coin_Flip_Config.super.onBackPressed());
    }

    private void setup_choose_picker() {
        Button choose_picker = findViewById(R.id.picker_button);
        choose_picker.setOnClickListener(view -> {
            Intent intent = new Intent(Coin_Flip_Config.this, Picker_Queue_Activity.class);
            intent.putExtra("CHILDREN_LIST", children_list);
            picker_launcher.launch(intent);
        });
    }


    private void setup_radio_group() {
        RadioButton heads = findViewById(R.id.radio_heads);
        heads.setOnClickListener(view -> guess = true);
        RadioButton tails = findViewById(R.id.radio_tails);
        tails.setOnClickListener(view -> guess = false);

    }
    private void setup_flip_coin() {

        Button button = findViewById(R.id.flip_coin_button);
        button.setOnClickListener(view -> {
            options = findViewById(R.id.config_guess_radio_group);
            if (options.getCheckedRadioButtonId() == -1)
            {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                Snackbar.make(view,"Select Heads or Tails!",Snackbar.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(Coin_Flip_Config.this,Coin_Flip_Activity.class);
                intent.putExtra("GUESS",guess);
                intent.putExtra("CHILD_NAME", picker_name.getText().toString());
                coin_flip_launcher.launch(intent);
            }
        });

    }

}