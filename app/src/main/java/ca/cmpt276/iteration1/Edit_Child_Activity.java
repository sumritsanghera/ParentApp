package ca.cmpt276.iteration1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

/*
    Edit Name Activity:
    -   Text input display tapped name
    -   Edit name, then press 'save' or 'delete' to save or delete name.
    -   Upon pressing back button, name will be reverted back to original.
 */

public class Edit_Child_Activity extends AppCompatActivity {

    private TextInputEditText text;
    private String original_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        setup_back_button();
        setup_edit_text();
        setup_save_button();
        setup_delete_button();
    }


    private void setup_back_button() {
        ImageView button = findViewById(R.id.edit_back_button);
        button.setOnClickListener(view -> Edit_Child_Activity.super.onBackPressed());
    }

    private void setup_edit_text() {
        Intent data = getIntent();
        text = findViewById(R.id.edit_name_edit_text);
        text.setText(data.getStringExtra("NAME"));
        original_name = data.getStringExtra("NAME");
    }


    private void setup_save_button() {
        Button submitBtn= findViewById(R.id.save_button);
        text = findViewById(R.id.edit_name_edit_text);
        submitBtn.setOnClickListener(view -> {
            if(String.valueOf(text.getText()).equals("")){
                Snackbar.make(view,"Cannot add empty name",Snackbar.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent();
                intent.putExtra("DELETE_BUTTON",false);
                intent.putExtra("NAME", String.valueOf(text.getText()));
                intent.putExtra("ORIGINAL_NAME",original_name);
                intent.putExtra("INDEX", getIntent().getIntExtra("INDEX",-1));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void setup_delete_button() {
        Button deleteBtn = findViewById(R.id.delete_button);
        deleteBtn.setOnClickListener(view -> new AlertDialog.Builder(Edit_Child_Activity.this)
                .setTitle("Delete name")
                .setMessage("Are you sure to delete this entry?")
                .setPositiveButton("DELETE", (dialog, which) -> {
                    Intent intent = new Intent();
                    intent.putExtra("DELETE_BUTTON",true);
                    intent.putExtra("INDEX", getIntent().getIntExtra("INDEX",-1));
                    setResult(RESULT_OK,intent);
                    finish();
                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.cancel())
                .setIcon(R.drawable.delete_icon)
                .show());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("EDIT_TEXT",String.valueOf(text.getText()));
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        text.setText(savedInstanceState.getString("EDIT_TEXT"));
    }
}