package ca.cmpt276.iteration1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_FOR_NAME = 101;

    public static Intent makeIntent(Context context) {
        return new Intent(context,MainActivity.class);
    }
    List<String > StrArr=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button add_name = (Button) findViewById(R.id.addName);
        add_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(MainActivity.this,"Clicked",Toast.LENGTH_SHORT).show();
                Intent intent=MainActivity2.makeIntent(MainActivity.this);
                startActivityForResult(intent, REQUEST_CODE_FOR_NAME);
            }
        });

        Button edit_name=(Button) findViewById(R.id.editName);
        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent=MainActivity3.makeIntent(MainActivity.this);
                startActivityForResult(intent, REQUEST_CODE_FOR_NAME);
            }
        });
        setup_back_button();
    }

    private void setup_back_button() {
        Button back_button = findViewById(R.id.back_btn);
        back_button.setOnClickListener(view -> MainActivity.super.onBackPressed());
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case REQUEST_CODE_FOR_NAME:
                if(resultCode== Activity.RESULT_OK){
                    String str=data.getStringExtra("NewName");
                    StrArr.add(str);
                    Log.i("MyApp", "Result is : " +  str);
                    Toast.makeText(MainActivity.this, "added name is " + str, Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "added name is " + StrArr.toString(), Toast.LENGTH_SHORT).show();

                }
                else{
                    Log.i("MyApp", "No result found");
                }
        }
    }
}