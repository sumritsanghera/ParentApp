package ca.cmpt276.iteration1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ca.cmpt276.iteration1.model.Child;
import ca.cmpt276.iteration1.model.Task;

public class Add_Task_Activity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> edit_launcher;
    private ArrayList<Child> children_list;
    private TextView name;
    private TextInputEditText description;
    private int chosen_child_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setup_getIntentData();
        setup_edit_launcher();

        setup_back_button();
        setup_profile();
        setup_edit_child();
        setup_add_task_button();
    }

    private void setup_getIntentData() {
        children_list = getIntent().getParcelableArrayListExtra("CHILDREN_LIST");
    }

    private void setup_edit_launcher() {
        edit_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null){
                            int index = data.getIntExtra("CHILD_INDEX",0);
                            name = findViewById(R.id.add_task_name);
                            name.setText(children_list.get(index).getName());
                            chosen_child_index = index;
                        }
                    }
                }
        );
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
    }

    private void setup_edit_child() {
        ImageView edit = findViewById(R.id.add_task_edit);
        edit.setOnClickListener(view -> {
            Intent intent = new Intent(Add_Task_Activity.this,Picker_Queue_Activity.class);
            intent.putExtra("CHILDREN_LIST",children_list);
            edit_launcher.launch(intent);
        });
    }

    //Return queue and task description
    private void setup_add_task_button() {
        description = findViewById(R.id.add_task_description);
        Button button = findViewById(R.id.add_task_submit_button);
        button.setOnClickListener(view -> {
            if(String.valueOf(description.getText()).equals("")){
                Snackbar.make(view,"Cannot add empty task",Snackbar.LENGTH_LONG).show();
            } else {
                if(chosen_child_index!=0){
                    Child update_queue_child = children_list.get(chosen_child_index);
                    children_list.remove(chosen_child_index);
                    children_list.add(0,update_queue_child);
                }
                Intent intent = new Intent();
                intent.putExtra("TASK_DESCRIPTION", String.valueOf(description.getText()));
                intent.putExtra("QUEUE", children_list);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }


}