package ca.cmpt276.iteration1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

/*
       Main Menu class using layout: activity_main_menu
       4 transparent buttons takes user to 4 different features of the app namely:
       -    Children configuration
       -    Coin flip history
       -    Coin flip
       -    Timer

 */

public class Main_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setup_coin_flip();
    }

    private void setup_coin_flip() {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}