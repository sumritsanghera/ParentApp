package ca.cmpt276.ParentApp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import ca.cmpt276.ParentApp.model.Child;
import ca.cmpt276.ParentApp.model.Task;

/*
    Edit Task Activity:
    -   Takes in the task index and parcelable task object
    -   Display tapped task
    -   Allow user to change task description
    -   Allow user to change child
    -   Allow user to delete a task, which shows a warning dialog before confirm
 */

public class Edit_Task_Activity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> edit_launcher;
    private TextView name;
    private ImageView profile;
    private TextInputEditText description_text_input;
    private Task given_task;
    private int chosen_child_index = 0;
    private int task_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        setup_params();

        setup_edit_launcher();

        setup_back_button();
        setup_profile();
        setup_description();
        setup_edit_child();
        setup_save_task_button();
        setup_delete_task_button();
    }

    private void setup_params() {

        task_index = getIntent().getIntExtra("INDEX",-1);
        given_task = getIntent().getParcelableExtra("TASK");
        name = findViewById(R.id.edit_task_name);
        profile = findViewById(R.id.edit_task_profile);

        assert task_index != -1;
        assert given_task != null;
    }


    private void setup_edit_launcher() {
        edit_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null){
                            int index = data.getIntExtra("CHILD_INDEX",0);
                            Child chosen_child = given_task.getQueue().get(index);
                            name.setText(chosen_child.getName());
                            if(chosen_child.getImagePath().equals("Default pic")){
                                profile.setImageResource(R.drawable.default_profile);
                            } else {
                                loadImageFromStorage(chosen_child.getImagePath(),
                                        chosen_child.getName(),
                                        profile);
                            }
                            chosen_child_index = index;
                        }
                    }
                }
        );
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.edit_task_back_button);
        back_button.setOnClickListener(view -> Edit_Task_Activity.super.onBackPressed());
    }

    private void setup_profile() {
        if(!given_task.getQueue().isEmpty()){
            name.setText(given_task.getQueue().get(0).getName());
            if(given_task.getImage().equals("Default pic")){
                profile.setImageResource(R.drawable.default_profile);
            } else {
                loadImageFromStorage(given_task.getImage(),
                                        given_task.getName(),
                                        profile);
            }
        }

        CardView cardView = findViewById(R.id.edit_task_card_view);
        int width = cardView.getLayoutParams().width;
        cardView.setRadius((float) width/2);
    }

    private void setup_description() {
        TextInputEditText text = findViewById(R.id.edit_task_description);
        text.setText(given_task.getTask_description());
    }

    private void setup_edit_child() {
        ImageView edit = findViewById(R.id.edit_task_edit_profile);
        edit.setOnClickListener(view -> {
            Intent intent = new Intent(Edit_Task_Activity.this,Picker_Queue_Activity.class);
            intent.putExtra("CHILDREN_LIST",given_task.getQueue());
            edit_launcher.launch(intent);
        });
    }

    //Return queue and task description
    private void setup_save_task_button() {
        description_text_input = findViewById(R.id.edit_task_description);
        Button button = findViewById(R.id.edit_task_submit_button);
        button.setOnClickListener(view -> {
            if(String.valueOf(description_text_input.getText()).equals("")){
                Snackbar.make(view,"Cannot save empty task",Snackbar.LENGTH_LONG).show();
            } else {
                Task new_task = new Task(given_task.getQueue(),String.valueOf(description_text_input.getText()));
                if(chosen_child_index!=0){
                    for(int i = 0; i < chosen_child_index; i++){
                        new_task.update_queue();
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("NEW_TASK", new_task);
                intent.putExtra("INDEX", task_index);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }


    private void setup_delete_task_button() {
        ImageView delete = findViewById(R.id.edit_task_delete);
        delete.setOnClickListener(view -> new AlertDialog.Builder(Edit_Task_Activity.this)
                .setTitle("Delete name")
                .setMessage("Are you sure to delete this task?")
                .setPositiveButton("DELETE", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.putExtra("DELETE",true);
                    intent.putExtra("INDEX", task_index);
                    Log.e("EDIT TASK ACTIVITY", ""+task_index);
                    setResult(RESULT_OK,intent);
                    finish();
                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.cancel())
                .setIcon(R.drawable.delete_icon)
                .show());
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