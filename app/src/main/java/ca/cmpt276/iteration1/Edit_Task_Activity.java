package ca.cmpt276.iteration1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import ca.cmpt276.iteration1.model.Task;

public class Edit_Task_Activity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> edit_launcher;
    private TextView name;
    private TextInputEditText description_text_input;
    private Task given_task;
    private int chosen_child_index = 0;
    private int task_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        setup_getIntentData();

        setup_edit_launcher();

        setup_back_button();
        setup_profile();
        setup_description();
        setup_edit_child();
        setup_edit_task_button();
        setup_delete_task_button();
    }

    private void setup_getIntentData() {

        task_index = getIntent().getIntExtra("INDEX",-1);
        given_task = getIntent().getParcelableExtra("TASK");

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
                            name = findViewById(R.id.edit_task_name);
                            name.setText(given_task.getQueue().get(index).getName());
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
        TextView name = findViewById(R.id.edit_task_name);
        if(!given_task.getQueue().isEmpty()){
            name.setText(given_task.getQueue().get(0).getName());
        }
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
    private void setup_edit_task_button() {
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

}