package ca.cmpt276.iteration1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Children_Config extends AppCompatActivity {

    private ActivityResultLauncher<Intent> add_name_launcher;
    private ArrayList<String> children_list;
    private ArrayAdapter<String> adapter;
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
            adapter = new ArrayAdapter<>(
                    this,
                    R.layout.children_list,
                    children_list);
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
//        setup_edit_list();
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

                            children_list.add(name);
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
                    assert data != null;

                    String name = data.getStringExtra("NAME");
                    int index = data.getIntExtra("INDEX",-1);

                    children_list.set(index,name);
                    setResult();
                });
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra("CHILDREN_LIST",children_list);
        setResult(RESULT_OK,intent);
    }

    private void setup_children_list() {
        Intent intent = getIntent();
        children_list = intent.getStringArrayListExtra("CHILDREN_LIST");
        if(!children_list.isEmpty()){
            adapter = new ArrayAdapter<>(
                    this,
                    R.layout.children_list,
                    children_list);
            ListView list = findViewById(R.id.children_listView);
            list.setAdapter(adapter);

        }
    }

//    private void setup_edit_list() {
//        ListView list = findViewById(R.id.children_listView);
//        list.setOnItemClickListener((parent, viewClicked, position, id) -> {
//            Intent intent = new Intent(Children_Config.this,Edit_Name_Activity.class);
//            intent.putExtra("NAME", children_list.get(position));
//            intent.putExtra("INDEX", position);
//            edit_name_launcher.launch(intent);
//        });
//    }

    private void setup_floating_button() {
        FloatingActionButton button = findViewById(R.id.add_child_floating_button);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Children_Config.this, Add_Name_Activity.class);
            add_name_launcher.launch(intent);
        });
    }
}