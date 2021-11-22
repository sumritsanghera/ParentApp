package ca.cmpt276.iteration1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private ActivityResultLauncher<Intent> edit_name_launcher;
    private ArrayList<Child> children_list;
    private ArrayList<Child> added_children;
    private ArrayList<Child> removed_children;
    private ArrayList<Edited_Child> edited_children;
    private Children_Config_Adapter adapter;
    private TextView info;

    @Override
    protected void onResume() {
        super.onResume();
        refresh_children_list();
    }

    private void refresh_children_list() {
        info = findViewById(R.id.config_info);
        if(!children_list.isEmpty()){
            info.setText(R.string.config_info_2);
        } else {
            info.setText(R.string.config_info);
        }
        adapter = new Children_Config_Adapter(this,children_list);
        GridView list = findViewById(R.id.children_grid_view);
        list.setAdapter(adapter);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        setup_fields();

        setup_back_button();
        setup_add_child_launcher();
        setup_edit_child_launcher();
        setup_children_list();
        setup_floating_button();
    }

    private void setup_fields() {
        info = findViewById(R.id.config_info);
        added_children = new ArrayList<>();
        removed_children = new ArrayList<>();
        edited_children = new ArrayList<>();
    }


    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.config_back_button);
        back_button.setOnClickListener(view -> Children_Config.super.onBackPressed());
    }

    private void setup_add_child_launcher() {
        add_name_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String name = data.getStringExtra("NAME");
                            String bitmap = data.getStringExtra("PICTURE");
                            Child new_child = new Child(name, bitmap);
                            children_list.add(new_child);
                            added_children.add(new_child);
                            refresh_children_list();
                            setResult();
                        }
                    }
                });
    }

    private void setup_edit_child_launcher() {
        edit_name_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if(data != null){
                        boolean option = data.getBooleanExtra("DELETE_BUTTON",true);
                        int index = data.getIntExtra("INDEX", -1);
                        if(!option) {
                            String name = data.getStringExtra("NAME");
                            String bitmap = data.getStringExtra("PICTURE");
                            Edited_Child new_child = new Edited_Child(children_list.get(index).getName(),name,
                                                            children_list.get(index).getImagePath(),bitmap);
                            edited_children.add(new_child);
                            Child edit_child = children_list.get(index);
                            edit_child.setName(name);
                            edit_child.setBitmap(bitmap);
                            children_list.set(index,edit_child);
                        } else {
                            removed_children.add(children_list.get(index));
                            children_list.remove(index);
                        }
                    }
                    refresh_children_list();
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
            info.setText(R.string.config_info_2);
            adapter = new Children_Config_Adapter(this,children_list);
            GridView list = findViewById(R.id.children_grid_view);
            list.setAdapter(adapter);

        }
    }


    private class Children_Config_Adapter extends BaseAdapter {
        Context context;
        private final ArrayList<Child> children_list;

        public Children_Config_Adapter(Context context, ArrayList<Child> values) {
            this.context = context;
            this.children_list = values;
        }

        @Override
        public int getCount() {
            return children_list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.children_config_grid_view_item,parent,false);
            }
            //populate the list
            //get current child
            Child current_child = children_list.get(position);
            String child_name = current_child.getName();

            //fill view

            CardView cardView = itemView.findViewById(R.id.grid_card_view);
            int width = cardView.getLayoutParams().width;
            cardView.setRadius((float) width/2);

            ImageView profile = itemView.findViewById(R.id.profile_icon);
            loadImageFromStorage(current_child.getImagePath(),current_child.getName(),profile);

            TextView nameView = itemView.findViewById(R.id.config_child_name);
            nameView.setText(child_name);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(Children_Config.this, Edit_Child_Activity.class);
                intent.putExtra("CHILD", current_child);
                intent.putExtra("IMAGE", current_child.getImagePath());
                Log.e("CHILDREN_CONFIG bitmap in adapter", current_child.getImagePath());
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