package ca.cmpt276.iteration1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;

import ca.cmpt276.iteration1.model.Child;

/*
    Edit Name Activity:
    -   Text input display tapped name
    -   Edit name, then press 'save' or 'delete' to save or delete name.
    -   Upon pressing back button, name will be reverted back to original.
 */

public class Edit_Child_Activity extends AppCompatActivity {

    private TextInputEditText text;
    private ImageView profilePicture;
    private AlertDialog alertDialogProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        setup_back_button();
        setup_profile();
        setup_camera_button();
        setup_save_button();
        setup_delete_button();
    }


    private void setup_back_button() {
        ImageView button = findViewById(R.id.edit_back_button);
        button.setOnClickListener(view -> Edit_Child_Activity.super.onBackPressed());
    }

    private void setup_profile() {
        Intent data = getIntent();
        Child current_child = data.getParcelableExtra("CHILD");

        text = findViewById(R.id.edit_name_edit_text);
        text.setText(current_child.getName());

        profilePicture = findViewById(R.id.edit_profile_picture);
        String image = data.getStringExtra("IMAGE");
        Bitmap bm = StringToBitMap(image);
        profilePicture.setImageBitmap(bm);
    }


    private void setup_camera_button() {
        profilePicture = findViewById(R.id.edit_profile_picture);
        profilePicture.setOnClickListener(view -> chooseProfilePicture());
    }


    private void chooseProfilePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Child_Activity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_profile_picture, null);

        builder.setCancelable(false);
        builder.setView(dialogView);
        builder.setTitle(R.string.add_image);

        ImageView camera_icon = dialogView.findViewById(R.id.imageView_CameraIcon);
        ImageView gallery_icon = dialogView.findViewById(R.id.imageView_GalleryIcon);

        alertDialogProfilePicture = builder.create();
        alertDialogProfilePicture.show();

        camera_icon.setOnClickListener(view -> {
            takePictureFromCamera();
            alertDialogProfilePicture.cancel();
            TextView text = findViewById(R.id.touch_edit_picture);
            text.setVisibility(View.INVISIBLE);
        });

        gallery_icon.setOnClickListener(view -> {
            takePictureFromGallery();
            alertDialogProfilePicture.cancel();
            TextView text = findViewById(R.id.touch_edit_picture);
            text.setVisibility(View.INVISIBLE);
        });
    }

    private void takePictureFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto,1);
    }

    private void takePictureFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePicture, 2);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                Uri selectedImageURI = data.getData();
                profilePicture.setImageURI(selectedImageURI);
            }
        }
        if (requestCode == 2) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                Bitmap bitmapImage = (Bitmap) bundle.get("data");
                profilePicture.setImageBitmap(bitmapImage);
            }
        }
    }

    public String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void setup_save_button(){
        Button submitBtn= findViewById(R.id.save_button);
        text = findViewById(R.id.edit_name_edit_text);
        submitBtn.setOnClickListener(view -> {
            if(String.valueOf(text.getText()).equals("")){
                Snackbar.make(view,"Cannot add empty name",Snackbar.LENGTH_LONG).show();
            } else {
                String original = String.valueOf(text.getText());
                String capitalizeFirstLetter = original.substring(0, 1).toUpperCase() + original.substring(1);

                Intent intent = new Intent();
                intent.putExtra("DELETE_BUTTON",false);
                intent.putExtra("NAME", capitalizeFirstLetter);

                profilePicture.buildDrawingCache();
                Bitmap bitmap = profilePicture.getDrawingCache();
                String code = bitmapToString(bitmap);
                intent.putExtra("PICTURE", code);

                intent.putExtra("INDEX", getIntent().getIntExtra("INDEX",-1));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void setup_delete_button() {
        ImageView deleteBtn = findViewById(R.id.delete_button);
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

    //https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
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