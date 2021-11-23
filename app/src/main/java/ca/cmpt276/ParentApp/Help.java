package ca.cmpt276.ParentApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

/*
    Help screen:
    -   Hook up all hyperlinks in help xml file, takes user to reference links upon a tap.
 */

public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        setup_back_button();
        setupHyperlinks();
    }

    private void setup_back_button() {
        ImageView button = findViewById(R.id.help_back_button);
        button.setOnClickListener(view -> Help.super.onBackPressed());
    }

    private void setupHyperlinks() {
        TextView author_ref = findViewById(R.id.author_ref);
        TextView txt1 = findViewById(R.id.ref1);
        TextView txt2 = findViewById(R.id.ref2);
        TextView txt3 = findViewById(R.id.ref3);
        TextView txt4 = findViewById(R.id.ref4);
        TextView txt5 = findViewById(R.id.ref5);
        TextView txt6 = findViewById(R.id.ref6);
        TextView txt7 = findViewById(R.id.ref7);
        TextView txt8 = findViewById(R.id.ref8);
        TextView txt9 = findViewById(R.id.ref9);
        TextView txt10 = findViewById(R.id.ref10);
        TextView txt11 = findViewById(R.id.ref11);
        TextView txt12 = findViewById(R.id.code_ref1);
        TextView txt13 = findViewById(R.id.code_ref2);
        TextView txt14 = findViewById(R.id.code_ref3);
        TextView txt15 = findViewById(R.id.code_ref4);


        author_ref.setMovementMethod(LinkMovementMethod.getInstance());
        txt1.setMovementMethod(LinkMovementMethod.getInstance());
        txt2.setMovementMethod(LinkMovementMethod.getInstance());
        txt3.setMovementMethod(LinkMovementMethod.getInstance());
        txt4.setMovementMethod(LinkMovementMethod.getInstance());
        txt5.setMovementMethod(LinkMovementMethod.getInstance());
        txt6.setMovementMethod(LinkMovementMethod.getInstance());
        txt7.setMovementMethod(LinkMovementMethod.getInstance());
        txt8.setMovementMethod(LinkMovementMethod.getInstance());
        txt9.setMovementMethod(LinkMovementMethod.getInstance());
        txt10.setMovementMethod(LinkMovementMethod.getInstance());
        txt11.setMovementMethod(LinkMovementMethod.getInstance());
        txt12.setMovementMethod(LinkMovementMethod.getInstance());
        txt13.setMovementMethod(LinkMovementMethod.getInstance());
        txt14.setMovementMethod(LinkMovementMethod.getInstance());
        txt15.setMovementMethod(LinkMovementMethod.getInstance());
    }
}