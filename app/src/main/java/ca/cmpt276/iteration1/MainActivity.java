package ca.cmpt276.iteration1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_FOR_NAME = 101;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode){
            case REQUEST_CODE_FOR_NAME:
                if(resultCode== Activity.RESULT_OK){
                    String str=data.getStringExtra("newName");
                    Log.i("MyApp", "Result is : " +  str);
                }
                else{
                    Log.i("MyApp", "No result found");
                }
        }
    }
}