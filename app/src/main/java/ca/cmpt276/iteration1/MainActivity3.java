package ca.cmpt276.iteration1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity3 extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context,MainActivity3.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        setupEndActivityButton();
    }
    private void setupEndActivityButton() {
        Button submitBtn= (Button) findViewById(R.id.SUBMIT1);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity3.this,"Canceled",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}