package ca.cmpt276.iteration1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import java.util.ArrayList;

import ca.cmpt276.iteration1.model.Child;
import ca.cmpt276.iteration1.model.Children_Manager;
import ca.cmpt276.iteration1.model.Coin_Flip;
import ca.cmpt276.iteration1.model.Coin_Flip_Manager;
import ca.cmpt276.iteration1.model.Coin_Queue_Manager;
import ca.cmpt276.iteration1.model.Edited_Child;
import ca.cmpt276.iteration1.model.Task;
import ca.cmpt276.iteration1.model.Task_Manager;

/*
       Main Menu class using layout: activity_main_menu
       5 transparent buttons takes user to 5 different features of the app namely:
       -    Children configuration
       -    Coin flip history
       -    Coin flip
       -    Timer
       -    Help

 */

public class Main_menu extends AppCompatActivity {
    private Children_Manager children_manager;
    private Coin_Flip_Manager coin_manager;
    private Task_Manager task_manager;
    private Coin_Queue_Manager coin_queue;
    private ActivityResultLauncher<Intent> coin_flip_launcher;
    private ActivityResultLauncher<Intent> config_launcher;
    private ActivityResultLauncher<Intent> task_launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        coin_queue = Coin_Queue_Manager.getInstance();
        children_manager = Children_Manager.getInstance();
        coin_manager = Coin_Flip_Manager.getInstance();
        task_manager = Task_Manager.getInstance();

        setup_coin_flip_launcher();
        setup_config_launcher();
        setup_task_launcher();

        setup_config_button();
        setup_coin_flip();
        setup_tasks();
        setup_history_button();
        setup_timer_button();

    }

    private void setup_config_launcher() {
        config_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        //if there's configured children
                        if(data != null){
                            ArrayList<Child> children_list = data.getParcelableArrayListExtra("CHILDREN_LIST");
                            ArrayList<Child> removed_list = data.getParcelableArrayListExtra("REMOVED_CHILDREN");
                            ArrayList<Edited_Child> edited_children_list = data.getParcelableArrayListExtra("EDITED_CHILDREN");

                            children_manager.clear();
                            for (Child new_child : children_list) {
                                children_manager.addChild(new_child);
                            }

                            //update coin_queue
                            if (coin_queue.getQueue().isEmpty()) {
                                //if queue is empty, set queue to be children list
                                coin_queue.setQueue(children_manager.getChildren_list());
                            } else {
                                //first, save edited names
                                coin_queue.update_child_name_after_edit(edited_children_list);

                                //then get the first child in queue and find its index in children manager
                                String firstChild = coin_queue.firstChild();
                                int found_index = children_manager.find_name(firstChild);

                                //set queue to be children list
                                coin_queue.setQueue(children_manager.getChildren_list());

                                //if name is found, then dequeue by number of index to move picker to front of line.
                                //else, leave queue same with children manager
                                if(found_index!=-1){
                                    for(int i = 0; i < found_index; i++){
                                        coin_queue.dequeue();
                                    }
                                }
                            }

                            //update coin_manager if there are flipped coins
                            if(!coin_manager.getCoin_flip_list().isEmpty()){
                                //update on edited names
                                coin_manager.update_child_name_after_edit(edited_children_list);

                                //remove coin flips if removed list is not empty.
                                if(!removed_list.isEmpty()){
                                    coin_manager.remove_child(removed_list);
                                }
                            }

                            //update tasks queues if there are existing tasks
                            if(!task_manager.getTask_list().isEmpty()){
                                //First update on any changed names
                                task_manager.update_child_name_after_edit(edited_children_list);

                                //for every task in task list
                                for(Task task : task_manager.getTask_list()){

                                    //get child in line
                                    String firstChild = task.getName();

                                    //if first child is no name, means task has no child, set task queue
                                    //equal to children list
                                    if(firstChild.equals("No name")){
                                        task.setQueue(children_manager.getChildren_list());
                                    } else { //if queue is not empty, find child first in line in queue of current
                                        //task and find its index in children list
                                        int found_index = children_manager.find_name(firstChild);

                                        //set queue to be children list
                                        task.setQueue(children_manager.getChildren_list());

                                        //if name is found, then dequeue by number of index to move picker to front of line.
                                        //else, leave queue same with children manager
                                        if(found_index!=-1){
                                            for(int i = 0; i < found_index; i++){
                                                task.update_queue();
                                            }
                                        }
                                    }
                                }
                            }


                        } else {
                            children_manager.clear();
                            coin_queue.clear();
                        }
                    }
                }
        );
    }

    private void setup_coin_flip_launcher() {
        coin_flip_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if(data!=null) {
                            String picker = data.getStringExtra("PICKER");
                            boolean returned_result = data.getBooleanExtra("RESULT", false);
                            String time = data.getStringExtra("TIME");

                            //move child to end of line after every flip
                            if (!picker.equals("No name")) {
                                int index = 0;
                                ArrayList<Child> children_list = coin_queue.getQueue();
                                for (int i = 0; i < children_list.size(); i++) {
                                    if (children_list.get(i).getName().equals(picker)) {
                                        index = i;
                                        break;
                                    }
                                }
                                Child dequeue_child = coin_queue.get(index);
                                coin_queue.remove(index);
                                coin_queue.add(dequeue_child); //move first child to end of line
                            }

                            Coin_Flip new_flip = new Coin_Flip(picker, returned_result, time);
                            coin_manager.add_flip(new_flip);
                        }
                    }
                });
    }

    private void setup_task_launcher() {
        task_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if(data!=null){
                            ArrayList<Task> task_list = data.getParcelableArrayListExtra("TASK_LIST");
                            task_manager.clear();
                            for(Task task : task_list){
                                task_manager.add_task(task);
                            }
                        }
                    }
                });
    }

    private void setup_config_button() {
        Button configure_children = findViewById(R.id.config_button);
        configure_children.setOnClickListener(view -> {
            Intent intent = new Intent(Main_menu.this, Children_Config.class);
            intent.putExtra("CHILDREN_LIST", children_manager.getChildren_list());
            config_launcher.launch(intent);
        });
    }

    private void setup_coin_flip() {
        Button button = findViewById(R.id.coin_flip_button);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Main_menu.this, Coin_Flip_Config.class);
            intent.putExtra("QUEUE",coin_queue.getQueue());
            coin_flip_launcher.launch(intent);
        });
    }

    private void setup_history_button() {
        Button button = findViewById(R.id.coin_history_button);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Main_menu.this, Coin_flip_history.class);
            intent.putExtra("COIN_FLIP_LIST", coin_manager.getCoin_flip_list());
            startActivity(intent);
        });
    }

    private void setup_tasks() {
        Button button = findViewById(R.id.task_button);
        button.setOnClickListener(view->{
            Intent intent = new Intent(Main_menu.this, Task_List_Activity.class);
            intent.putParcelableArrayListExtra("CHILDREN_LIST",children_manager.getChildren_list());
            intent.putParcelableArrayListExtra("TASK_LIST", task_manager.getTask_list());

            task_launcher.launch(intent);
        });
    }

    private void setup_timer_button() {
        Button button = findViewById(R.id.timer_button);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Main_menu.this, Timeout_Timer.class);
            startActivity(intent);
        });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}