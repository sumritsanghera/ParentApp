package ca.cmpt276.ParentApp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import ca.cmpt276.ParentApp.model.Child;

public class Coin_Flip_Config extends AppCompatActivity {

    private ArrayList<Child> queue;
    private Child current_child;
    private ActivityResultLauncher<Intent> picker_launcher;
    private ActivityResultLauncher<Intent> coin_flip_launcher;
    private TextView picker_name;
    private RadioGroup options;
    private Boolean guess; //true is heads, false is tails.
    private int index_picker = 0; //index of picker in queue
    private ImageView picker_picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_config);

        setup_params();
        setup_default_picker();

        setup_choose_picker_launcher();
        setup_coin_flip_launcher();

        setup_back_button();
        setup_choose_picker();
        setup_radio_group();
        setup_flip_coin();
    }

    private void setup_params() {
        Intent data = getIntent();
        queue = data.getParcelableArrayListExtra("QUEUE");
        Child no_name = new Child("No name");
        queue.add(no_name);
        current_child = queue.get(0);
    }

    private void setup_default_picker() {

        CardView profile = findViewById(R.id.coin_config_card_view);
        int width = profile.getLayoutParams().width;
        profile.setRadius((float) width/2);
        picker_name = findViewById(R.id.picker_name);
        picker_name.setText(current_child.getName());

        picker_picture = findViewById(R.id.picker_profile);
        String image = current_child.getImagePath();
        loadImageFromStorage(image,current_child.getName(), picker_picture);

    }

    private void setup_choose_picker_launcher() {
        picker_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null){
                            int index = data.getIntExtra("CHILD_INDEX",0);
                            current_child = queue.get(index);
                            picker_name = findViewById(R.id.picker_name);
                            picker_name.setText(current_child.getName());
                            if(queue.get(index).getImagePath().equals("Default pic")){
                                picker_picture.setImageResource(R.drawable.default_profile);
                            } else {
                                loadImageFromStorage(current_child.getImagePath(),
                                        current_child.getName(),
                                        picker_picture);
                            }
                            index_picker = index;
                        }
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
                        if(data!=null){
                            Intent intent = new Intent();
                            intent.putExtra("CHILD",current_child);
                            intent.putExtra("RESULT",data.getBooleanExtra("RESULT",false));
                            intent.putExtra("TIME", data.getStringExtra("TIME"));
                            intent.putExtra("INDEX",index_picker);
                            setResult(RESULT_OK,intent);
                            finish();
                        }

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
            intent.putExtra("CHILDREN_LIST", queue);
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
                intent.putExtra("CHILD",current_child);
                coin_flip_launcher.launch(intent);
            }
        });

    }

    //pass in filename (which is child's name) and the image path to load image.
    private void loadImageFromStorage(String path, String filename, ImageView imageView)
    {

        try {
            File f=new File(path, filename + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}