package ca.cmpt276.ParentApp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.transition.Explode;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import ca.cmpt276.ParentApp.model.Child;
import ca.cmpt276.ParentApp.model.Task;

/*
    Task List Activity:
    -   Display all added tasks.
    -   Upon a tap on a task, popup inflates to show user task description, current child and
        their photo
           -> popup includes a button to cancel popup, or press "Turn done" and child jumps
            to next child in line of task queue.
    -   Allow user to add by pressing floating button
    -   Allow user to edit task by pressing on pencil icon.
 */

public class Task_List_Activity extends AppCompatActivity {
    private ArrayList<Task> task_list;
    private ArrayList<Child> children_list;
    private ActivityResultLauncher<Intent> add_task_launcher;
    private ActivityResultLauncher<Intent> edit_task_launcher;
    private ArrayAdapter<Task> adapter;

    @Override
    protected void onResume() {
        super.onResume();
        refreshTaskList();
    }

    private void refreshTaskList() {
        TextView info = findViewById(R.id.task_list_info_text);
        if(!task_list.isEmpty()){
            info.setText("");
        } else {
            info.setText(R.string.task_list_info_1);
        }
        adapter = new Task_List_Activity.Task_List_Adapter();
        ListView list = findViewById(R.id.task_list);
        list.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        // set an exit transition
        getWindow().setExitTransition(new Explode());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        setup_getIntentData();

        setup_add_task_launcher();
        setup_edit_task_launcher();

        setup_back_button();
        setup_task_list();
        setup_add_task_floating_button();
    }

    private void setup_getIntentData() {
        task_list = getIntent().getParcelableArrayListExtra("TASK_LIST");
        children_list = getIntent().getParcelableArrayListExtra("CHILDREN_LIST");
    }

    private void setup_add_task_launcher() {
        add_task_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            ArrayList<Child> new_queue = data.getParcelableArrayListExtra("QUEUE");
                            String description = data.getStringExtra("TASK_DESCRIPTION");
                            Task new_task = new Task(new_queue,description);
                            task_list.add(new_task);
                            setReturnResult();
                        }
                    }
                });
    }

    private void setup_edit_task_launcher() {
        edit_task_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            boolean delete = data.getBooleanExtra("DELETE",false);
                            int task_index = data.getIntExtra("INDEX",-1);
                            assert task_index != -1;
                            if(delete){
                                task_list.remove(task_index);
                            } else {
                                Task new_task = data.getParcelableExtra("NEW_TASK");
                                task_list.remove(task_index);
                                task_list.add(task_index,new_task);
                            }
                            setReturnResult();
                        }
                    }
                });
    }

    private void setReturnResult(){
        Intent intent = new Intent();
        intent.putExtra("TASK_LIST",task_list);
        setResult(RESULT_OK,intent);
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.task_back_button);
        back_button.setOnClickListener(view -> Task_List_Activity.super.onBackPressed());
    }

    private void setup_task_list() {
        if (!task_list.isEmpty()) {
            TextView text = findViewById(R.id.task_list_info_text);
            text.setText("");

            adapter = new Task_List_Adapter();
            ListView list = findViewById(R.id.task_list);
            list.setAdapter(adapter);
        }
    }

    private void setup_add_task_floating_button() {
        FloatingActionButton button = findViewById(R.id.task_add_floating_button);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Task_List_Activity.this,Add_Task_Activity.class);
            if(!children_list.isEmpty()){
                intent.putExtra("CHILDREN_LIST",children_list);
            }
            add_task_launcher.launch(intent);
        });
    }


    private class Task_List_Adapter extends ArrayAdapter<Task> {

        LayoutInflater layoutInflater;

        public Task_List_Adapter() {
            super(Task_List_Activity.this,
                    R.layout.task_item,
                    task_list);
            layoutInflater = LayoutInflater.from(Task_List_Activity.this);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //Make sure we have a view to work with (could be null)
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.task_item,parent,false);
            }
            //populate the list
            //get current coin_flip

            Task current_task = task_list.get(position);

            //fill view
            ImageView profile = itemView.findViewById(R.id.add_task_profile);
            loadImageFromStorage(current_task.getImage(),current_task.getImage(),profile);

            TextView nameView = itemView.findViewById(R.id.task_name);

            if(task_list.get(position).getQueue().size() > 0) {
                nameView.setText(current_task.getQueue().get(0).getName());
            }
            TextView descriptionView = itemView.findViewById(R.id.task_description);
            descriptionView.setText(current_task.getTask_description());

            final View inflate_view = itemView;

            RelativeLayout inflate_item = itemView.findViewById(R.id.task_item_relative_layout);
            inflate_item.setOnClickListener(view -> showPopupWindow(inflate_view,position,nameView,
                                                                    current_task));

            ImageView edit_button = itemView.findViewById(R.id.task_edit);
            edit_button.setOnClickListener(view -> {
                Intent intent = new Intent(Task_List_Activity.this,Edit_Task_Activity.class);
                intent.putExtra("INDEX",position);
                intent.putExtra("TASK",current_task);
                edit_task_launcher.launch(intent);
            });

            return itemView;
        }

        public void showPopupWindow(View view,int position,TextView task_name, Task currentTask) {

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.task_inflate_layout, null);

            // create the popup window
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screen_height = displayMetrics.heightPixels;
            int screen_width = displayMetrics.widthPixels;
            int popup_height = (int) (screen_height * 0.7);
            int popup_width = (int) (screen_width * 0.7);
            final PopupWindow popupWindow = new PopupWindow(popupView, popup_width, popup_height, true);
            popupWindow.setAnimationStyle(R.style.popup_animation);

            CardView profile = popupView.findViewById(R.id.inflate_card_view);
            profile.getLayoutParams().width = (int) (screen_width * 0.3);
            profile.getLayoutParams().height = profile.getLayoutParams().width;
            profile.setRadius(profile.getLayoutParams().width /(float)2);

            ImageView imageView = popupView.findViewById(R.id.inflate_profile);
            loadImageFromStorage(currentTask.getImage(),currentTask.getName(),imageView);

            TextView name = popupView.findViewById(R.id.inflate_text);
            if(currentTask.getQueue().isEmpty()){
                name.setText(R.string.inflate_default_name);
            } else {
                name.setText(currentTask.getQueue().get(0).getName());
            }

            TextView description = popupView.findViewById(R.id.inflate_task_name);
            description.setText(currentTask.getTask_description());

            // show the popup window
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            popupWindow.setOutsideTouchable(true);

            // dismiss the popup window when touched outside
            popupView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
                        popupWindow.dismiss();
                    }
                    return false;
                }
            });

            Button cancel = popupView.findViewById(R.id.inflate_cancel_button);
            cancel.setOnClickListener(button_view -> popupWindow.dismiss());

            Button turn_over = popupView.findViewById(R.id.inflate_turn_button);
            turn_over.setOnClickListener(button_view -> {
                if(!task_list.get(position).getQueue().isEmpty()) {
                    updateQueue(position, task_name);
                    refreshTaskList();
                    setReturnResult();
                }
                popupWindow.dismiss();
            });
        }
    }

    private void updateQueue(int position, TextView name) {
        Task task = task_list.get(position);
        task.update_queue();
        name.setText(task.getQueue().get(0).getName());
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