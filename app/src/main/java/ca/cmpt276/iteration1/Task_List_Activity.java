package ca.cmpt276.iteration1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import ca.cmpt276.iteration1.model.Task;

public class Task_List_Activity extends AppCompatActivity {
    ArrayList<Task> task_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        // set an exit transition
        getWindow().setExitTransition(new Explode());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        setup_back_button();
        setup_get_task_list();
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.task_back_button);
        back_button.setOnClickListener(view -> Task_List_Activity.super.onBackPressed());
    }

    private void setup_get_task_list() {
        task_list = getIntent().getParcelableArrayListExtra("TASK_LIST");

        ArrayAdapter<Task> adapter = new Task_List_Adapter();
        ListView list = findViewById(R.id.task_listView);
        list.setAdapter(adapter);
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

            //fill view
            TextView nameView = itemView.findViewById(R.id.task_name);

            if(task_list.get(position).getQueue().size() > 0) {
                nameView.setText(task_list.get(position).getQueue().get(0));
            }
            TextView descriptionView = itemView.findViewById(R.id.task_description);
            descriptionView.setText(task_list.get(position).getTask_description());

            final View inflate_view = itemView;

            RelativeLayout inflate_item = itemView.findViewById(R.id.task_item_relative_layout);
            inflate_item.setOnClickListener(view -> showPopupWindow(inflate_view));

            ImageView edit_button = itemView.findViewById(R.id.task_edit);
            edit_button.setOnClickListener(view -> Snackbar.make(view,"Edit the task!",Snackbar.LENGTH_LONG).show());

            return itemView;
        }

        public void showPopupWindow(View view) {

            // inflate the layout of the popup window
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.task_inflate_layout, null);

            // create the popup window
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            height -= 400;
            width -= 200;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
            popupWindow.setAnimationStyle(R.style.popup_animation);
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
            cancel.setOnClickListener(button_view -> {
                Snackbar.make(view,"You clicked cancel!",Snackbar.LENGTH_LONG).show();
                popupWindow.dismiss();
            });

            Button turn_over = popupView.findViewById(R.id.inflate_turn_button);
            turn_over.setOnClickListener(button_view -> {
                Snackbar.make(view,"You clicked turn over!",Snackbar.LENGTH_LONG).show();
                popupWindow.dismiss();
            });
        }
    }
}