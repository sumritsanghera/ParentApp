package ca.cmpt276.ParentApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import ca.cmpt276.ParentApp.model.Task_History_Item;

public class Task_History extends AppCompatActivity {

    private ListView task_listView;
    private ArrayList<Task_History_Item> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);

        setup_params();

        setup_back_button();
        setup_history_list();
    }

    private void setup_params() {
        task_listView = findViewById(R.id.task_history_listView);
        if(getIntent().getParcelableArrayListExtra("TASK_HISTORY") == null){
            history = new ArrayList<>();
        } else {
            history = getIntent().getParcelableArrayListExtra("TASK_HISTORY");
        }
        TextView history_info = findViewById(R.id.task_history_info);
        if(history.size() > 0){
            history_info.setText("");
        } else {
            history_info.setText(R.string.task_history_info);
        }
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.task_history_back_button);
        back_button.setOnClickListener(view -> Task_History.super.onBackPressed());
    }

    private void setup_history_list() {
        ArrayAdapter<Task_History_Item> adapter = new Task_History_Adapter();
        task_listView.setAdapter(adapter);
    }


    private class Task_History_Adapter extends ArrayAdapter<Task_History_Item>{
        public Task_History_Adapter() {
            super(Task_History.this,
                    R.layout.task_history_item,
                    history);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //Make sure we have a view to work with (could be null)
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.task_history_item,parent,false);
            }
            //populate the list
            //get current coin_flip
            Task_History_Item current_task = history.get(position);

            //fill view

            //edit width/height of profile picture
            CardView cardView = itemView.findViewById(R.id.task_history_cardView);
            int width = cardView.getLayoutParams().width;
            cardView.setRadius((float) width/2);

            //fill profile picture
            ImageView profile = itemView.findViewById(R.id.task_history_profile);
            Log.e("TASK_HISTORY",current_task.getImage());
            if(current_task.getImage().equals("Default pic")){
                profile.setImageResource(R.drawable.default_profile);
            } else {
                loadImageFromStorage(current_task.getImage(),current_task.getName(),profile);
            }

            //fill task time
            TextView timeView = itemView.findViewById(R.id.task_history_time);
            timeView.setText(current_task.getTime());

            //fill task name
            TextView nameView = itemView.findViewById(R.id.task_history_name);
            nameView.setText(current_task.getName());

            return itemView;
        }
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