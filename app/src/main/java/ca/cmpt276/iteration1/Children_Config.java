package ca.cmpt276.iteration1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ca.cmpt276.iteration1.model.Child;
import ca.cmpt276.iteration1.model.Edited_Child;

/*
    Children_Config
    -   Displays list of added names.
    -   Upon tap on a name, moves user to edit name activity for edit/delete.
    -   Includes a floating action bar to add names to list.
    -   List is then sent back to main menu as an ArrayList<String> for use in other
            features such as coin flip and coin flip history.
 */

public class Children_Config extends AppCompatActivity {

    private ActivityResultLauncher<Intent> add_name_launcher;
    private ArrayList<Child> children_list;
    private final ArrayList<Child> added_children = new ArrayList<>();
    private final ArrayList<Child> removed_children = new ArrayList<>();
    private final ArrayList<Edited_Child> edited_children = new ArrayList<>();
    private ArrayAdapter<Child> adapter;
    private ActivityResultLauncher<Intent> edit_name_launcher;
    private TextView info;

    @Override
    protected void onResume() {
        super.onResume();
        refresh_children_list();
    }

    private void refresh_children_list() {
        info = findViewById(R.id.config_info);
        if(!children_list.isEmpty()){
            info.setText("");
            adapter = new Children_Config_Adapter();
            ListView list = findViewById(R.id.children_listView);
            list.setAdapter(adapter);
        } else {
            info.setText(R.string.config_info);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        setup_back_button();
        setup_add_name_launcher();
        setup_edit_name_launcher();
        setup_children_list();
        setup_floating_button();
    }


    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.config_back_button);
        back_button.setOnClickListener(view -> Children_Config.super.onBackPressed());
    }

    private void setup_add_name_launcher() {
        add_name_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String name = data.getStringExtra("NAME");
                            Child new_child = new Child(name);
                            children_list.add(new_child);
                            added_children.add(new_child);
                            refresh_children_list();
                            setResult();
                        }
                    }
                });
    }

    private void setup_edit_name_launcher() {
        edit_name_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if(data != null){
                        boolean option = data.getBooleanExtra("DELETE_BUTTON",true);
                        int index = data.getIntExtra("INDEX", -1);
                        if(!option) {
                            String name = data.getStringExtra("NAME");
                            Edited_Child new_child = new Edited_Child(children_list.get(index).getName(),name);
                            edited_children.add(new_child);
                            Child edit_child = children_list.get(index);
                            edit_child.setName(name);
                            children_list.set(index,edit_child);
                        } else {
                            removed_children.add(children_list.get(index));
                            children_list.remove(index);
                        }
                    }

                    if(!children_list.isEmpty())
                        refresh_children_list();
                    else {
                        adapter.clear();
                        info.setText(R.string.config_info);
                    }
                    setResult();

                });
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra("CHILDREN_LIST",children_list);
        intent.putExtra("ADDED_CHILDREN", added_children);
        intent.putExtra("REMOVED_CHILDREN",removed_children);
        intent.putExtra("EDITED_CHILDREN",edited_children);
        setResult(RESULT_OK,intent);
    }

    private void setup_children_list() {
        Intent intent = getIntent();
        children_list = intent.getParcelableArrayListExtra("CHILDREN_LIST");
        if(!children_list.isEmpty()){
            adapter = new Children_Config_Adapter();
            ListView list = findViewById(R.id.children_listView);
            list.setAdapter(adapter);

        }
    }


    private class Children_Config_Adapter extends ArrayAdapter<Child> {
        public Children_Config_Adapter() {
            super(Children_Config.this,
                    R.layout.children_list,
                    children_list);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //Make sure we have a view to work with (could be null)
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.children_list,parent,false);
            }
            //populate the list
            //get current coin_flip

            //fill view

            TextView nameView = itemView.findViewById(R.id.children_config_list_name);
            nameView.setText(children_list.get(position).getName());

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(Children_Config.this, Edit_Child_Activity.class);
                intent.putExtra("NAME", children_list.get(position).getName());
                intent.putExtra("INDEX", position);
                edit_name_launcher.launch(intent);
            });

            return itemView;
        }
    }

    private void setup_floating_button() {
        FloatingActionButton button = findViewById(R.id.add_child_floating_button);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Children_Config.this, Add_Child_Activity.class);
            add_name_launcher.launch(intent);
        });
    }
}