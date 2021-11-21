package ca.cmpt276.iteration1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;

/*
    Add Child Activity
    -   Takes in the tapped name in configuration activity, and display in edit text.
    -   User can edit text and save name or delete name
    -   When add an empty name, snackbar appear to inform user action can not be done.

 */

public class Add_Child_Activity extends AppCompatActivity {

    ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);

        profilePicture = findViewById(R.id.picker_profile);

        setup_back_button();
        setup_submit_button();
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.add_name_back_button);
        back_button.setOnClickListener(view -> Add_Child_Activity.super.onBackPressed());
    }

    private void setup_submit_button() {
        Button button = findViewById(R.id.add_name_button);
        button.setOnClickListener(view -> {
            TextInputEditText input = findViewById(R.id.add_name_edit_text);
            if(String.valueOf(input.getText()).equals("")){
                Snackbar.make(view,"Cannot add empty name",Snackbar.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent();
                intent.putExtra("NAME", String.valueOf(input.getText()));
                setResult(RESULT_OK,intent);

//                Intent picture = new Intent();
//                String code = BitMapToString(((BitmapDrawable) profilePicture.getDrawable()).getBitmap());
//                picture.putExtra("PICTURE", code);
//                setResult(RESULT_OK, picture);

                finish();
            }
        });
    }

    //https://stackoverflow.com/questions/9224056/android-bitmap-to-base64-string
    //https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


}