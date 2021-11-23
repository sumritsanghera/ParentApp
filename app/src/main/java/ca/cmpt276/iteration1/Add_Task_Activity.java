package ca.cmpt276.iteration1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import ca.cmpt276.iteration1.model.Child;
import ca.cmpt276.iteration1.model.Task;

public class Add_Task_Activity extends AppCompatActivity {

    private ArrayList<Child> children_list;
    private TextInputEditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setup_getIntentData();

        setup_back_button();
        setup_profile();
        setup_add_task_button();
    }

    private void setup_getIntentData() {
        children_list = getIntent().getParcelableArrayListExtra("CHILDREN_LIST");
        if(children_list == null){
            children_list = new ArrayList<>();
            Child no_name = new Child("No name");
            children_list.add(no_name);
        }
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.add_task_back_button);
        back_button.setOnClickListener(view -> Add_Task_Activity.super.onBackPressed());
    }

    private void setup_profile() {
        TextView name = findViewById(R.id.add_task_name);
        if(!children_list.isEmpty()){
            name.setText(children_list.get(0).getName());
        }
        CardView cardView = findViewById(R.id.add_task_card_view);
        int width = cardView.getLayoutParams().width;
        cardView.setRadius((float) width/2);

        ImageView imageView = findViewById(R.id.add_task_profile);
        loadImageFromStorage(children_list.get(0).getImagePath(),
                            children_list.get(0).getName(),
                            imageView);

    }

    //Return queue and task description
    private void setup_add_task_button() {
        description = findViewById(R.id.add_task_description);
        Button button = findViewById(R.id.add_task_submit_button);
        button.setOnClickListener(view -> {
            if(String.valueOf(description.getText()).equals("")){
                Snackbar.make(view,"Cannot add empty task",Snackbar.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent();
                intent.putExtra("TASK_DESCRIPTION", String.valueOf(description.getText()));
                intent.putExtra("QUEUE", children_list);
                setResult(RESULT_OK,intent);
                finish();
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