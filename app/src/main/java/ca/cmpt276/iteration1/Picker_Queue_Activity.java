package ca.cmpt276.iteration1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import ca.cmpt276.iteration1.model.Child;

public class Picker_Queue_Activity extends AppCompatActivity {

    ArrayList<Child> children_list;
    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_queue);
        setup_back_button();

        setup_setIntentData();

        setup_queue_listView();
        setup_save_button();
    }

    private void setup_setIntentData() {
        children_list = getIntent().getParcelableArrayListExtra("CHILDREN_LIST");
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.choose_picker_back_button);
        back_button.setOnClickListener(view -> Picker_Queue_Activity.super.onBackPressed());
    }
    private void setup_queue_listView() {
        ArrayAdapter<Child> adapter = new Picker_Queue_Adapter();
        ListView list = findViewById(R.id.choose_picker_list);
        list.setAdapter(adapter);
    }

    private class Picker_Queue_Adapter extends ArrayAdapter<Child> {
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
            CardView cardView = itemView.findViewById(R.id.picker_card_view);
            int width = cardView.getLayoutParams().width;
            cardView.setRadius((float) width/2);

            ImageView imageView = itemView.findViewById(R.id.queue_profile);
            imageView.setImageResource(R.drawable.default_profile);

            TextView nameView = itemView.findViewById(R.id.queue_name);
            nameView.setText(children_list.get(position).getName());

            itemView.setOnClickListener(view -> {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View listItem = parent.getChildAt(i);
                    listItem.setBackgroundColor(getColor(R.color.white));
                }
                getWindow().getDecorView().setBackgroundColor(getColor(R.color.white));
                parent.getChildAt(position).setBackgroundResource(R.drawable.coin_flip_queue_selected_background);
                index = position;
            });

            return itemView;
        }
    }


    private void setReturn_Result(int index){
        Intent intent = new Intent();
        intent.putExtra("CHILD_INDEX",index);
        setResult(RESULT_OK,intent);
    }


    private void setup_save_button() {
        Button save = findViewById(R.id.queue_save);
        save.setOnClickListener(view -> {
            if(index == -1){
                Snackbar.make(view,"Choose a child before save",Snackbar.LENGTH_LONG).show();
            } else {
                setReturn_Result(index);
                finish();
            }
        });
    }


}