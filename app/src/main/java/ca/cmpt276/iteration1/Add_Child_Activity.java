package ca.cmpt276.iteration1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


/*
    Add Child Activity
    -   Takes in the tapped name in configuration activity, and display in edit text.
    -   User can edit text and save name or delete name
    -   When add an empty name, snackbar appear to inform user action can not be done.

    References:
    -   https://stackoverflow.com/questions/4715044/android-how-to-convert-whole-imageview-to-bitmap
    -   https://stackoverflow.com/questions/9224056/android-bitmap-to-base64-string
    -   https://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
    -   https://www.youtube.com/watch?v=foOp5Dq1Ypk
    -   https://www.youtube.com/watch?v=7v9afVcQCxw
    -   https://www.youtube.com/watch?v=2tRw6Q2JXGo

 */

public class Add_Child_Activity extends AppCompatActivity {

    ImageView profilePicture;
    private static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String IN_PATH = Environment.getDataDirectory().getPath();
    AlertDialog alertDialogProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        profilePicture = findViewById(R.id.profile_picture);

        setup_back_button();
        setup_submit_button();
        setup_camera_button();
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
                String original = String.valueOf(input.getText());
                String capitalizeFirstLetter = original.substring(0, 1).toUpperCase() + original.substring(1);

                Intent intent = new Intent();
                intent.putExtra("NAME", capitalizeFirstLetter);

                profilePicture.buildDrawingCache();
                Bitmap bitmap = profilePicture.getDrawingCache();
                String code = bitmapToString(bitmap);
                intent.putExtra("PICTURE", code);
                Log.i("TAG", "picture added to child");

                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void setup_camera_button() {
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();
            }
        });
    }

    private void chooseProfilePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Add_Child_Activity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_profile_picture, null);

        builder.setCancelable(false);
        builder.setView(dialogView);
        builder.setTitle(R.string.add_image);

        ImageView camera_icon = dialogView.findViewById(R.id.imageView_CameraIcon);
        ImageView gallery_icon = dialogView.findViewById(R.id.imageView_GalleryIcon);

        alertDialogProfilePicture = builder.create();
        alertDialogProfilePicture.show();

        camera_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromCamera();
                alertDialogProfilePicture.cancel();
                TextView text = findViewById(R.id.touch_add_picture);
                text.setVisibility(View.INVISIBLE);
            }
        });

        gallery_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureFromGallery();
                alertDialogProfilePicture.cancel();
                TextView text = findViewById(R.id.touch_add_picture);
                text.setVisibility(View.INVISIBLE);
            }
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

    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    public static String saveBitmap(Context context, Bitmap photo) {

        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }

        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filePic.getAbsolutePath();
    }

}