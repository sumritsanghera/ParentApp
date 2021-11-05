package ca.cmpt276.iteration1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import cmpt276.groupassignment.parentapp.model.Coin_Flip;
import cmpt276.groupassignment.parentapp.model.Coin_Flip_Manager;

/*
       Main Menu class using layout: activity_main_menu
       4 transparent buttons takes user to 4 different features of the app namely:
       -    Children configuration
       -    Coin flip history
       -    Coin flip
       -    Timer

 */

public class Main_menu extends AppCompatActivity {
    private Coin_Flip_Manager manager;
    private ActivityResultLauncher<Intent> coin_flip_launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setup_launcher();
        setup_coin_flip();
        setup_history_button();
    }

    private void setup_launcher() {
        coin_flip_launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;

                        String picker = data.getStringExtra("PICKER");
                        boolean returned_result = data.getBooleanExtra("RESULT",false);
                        String time = data.getStringExtra("TIME");

                        Coin_Flip new_flip = new Coin_Flip(picker,returned_result,time);
                        manager.add_flip(new_flip);
                    }
                });
    }

    private void setup_coin_flip() {

    }

    private void setup_history_button() {
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}