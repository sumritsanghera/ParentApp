package ca.cmpt276.iteration1.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import ca.cmpt276.iteration1.R;


public class Timer_Message_Fragment extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Create View to Show
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.message_layout, null);

        //Create button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("TAG", "you clicked the dialog button");
            }
        };

        //Build alert Dialog
        return new AlertDialog.Builder(getActivity())
                .setTitle("Timeout is Complete")
                .setView(v)
                .setPositiveButton(android.R.string.ok,listener)
                .create();
    }
}


