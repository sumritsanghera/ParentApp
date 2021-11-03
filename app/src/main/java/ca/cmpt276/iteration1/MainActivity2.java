package ca.cmpt276.iteration1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context,MainActivity2.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setupEndActivityButton();
    }

    private void setupEndActivityButton() {
        Button submitBtn= (Button) findViewById(R.id.SUBMIT);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit=(EditText) findViewById(R.id.textInputEditText);
                String textInputEditText=edit.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("NewName", textInputEditText);
                setResult(Activity.RESULT_OK, intent);
                Toast.makeText(MainActivity2.this,"Added new name",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}