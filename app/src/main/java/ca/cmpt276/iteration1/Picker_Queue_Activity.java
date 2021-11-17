package ca.cmpt276.iteration1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Picker_Queue_Activity extends AppCompatActivity {

    ArrayList<String> children_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_queue);
        setup_back_button();

        setup_queue_listView();
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.choose_picker_back_button);
        back_button.setOnClickListener(view -> Picker_Queue_Activity.super.onBackPressed());
    }
    private void setup_queue_listView() {
        Intent data = getIntent();
        children_list = data.getStringArrayListExtra("CHILDREN_LIST");
        Log.e("QUEUE", children_list.get(0));

        ArrayAdapter adapter = new Picker_Queue_Adapter();
        ListView list = findViewById(R.id.choose_picker_list);
        list.setAdapter(adapter);

    }

    private class Picker_Queue_Adapter extends ArrayAdapter<String> {
        public Picker_Queue_Adapter() {
            super(Picker_Queue_Activity.this,
                    R.layout.queue_item,
                    children_list);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //Make sure we have a view to work with (could be null)
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.queue_item,parent,false);
            }
            //populate the list
            //get current coin_flip

            //fill view
            ImageView imageView = itemView.findViewById(R.id.queue_profile);
            imageView.setImageDrawable(getDrawable(R.drawable.default_profile));

            TextView nameView = itemView.findViewById(R.id.queue_name);
            nameView.setText(children_list.get(position));

            itemView.setOnClickListener(view -> {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View listItem = parent.getChildAt(i);
                    listItem.setBackgroundColor(getColor(R.color.white));
                }
                getWindow().getDecorView().setBackgroundColor(getColor(R.color.white));
                parent.getChildAt(position).setBackgroundResource(R.drawable.coin_flip_queue_selected_background);
                setReturn_Result(position);
            });

            return itemView;
        }
    }


    private void setReturn_Result(int index){
        Intent intent = new Intent();
        intent.putExtra("CHILD_INDEX",index);
        setResult(RESULT_OK,intent);
    }

}