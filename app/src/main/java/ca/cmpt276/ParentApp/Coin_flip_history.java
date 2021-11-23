package ca.cmpt276.ParentApp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import ca.cmpt276.ParentApp.model.Coin_Flip;

/*
    Coin Flip History:
    -   Lists all coin flips, with their flip time, picker's name and result.
    -   Flip animation starts when tapped on coin
    -   Result is displayed as check mark or clear mark, depending if result match picker's guess.

 */

public class Coin_flip_history extends AppCompatActivity {

    private ArrayList<Coin_Flip> coin_list;

    @Override
    protected void onResume() {
        super.onResume();
        setup_history_list();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_history);

        setup_back_button();
        setup_history_list();
    }

    private void setup_back_button() {
        ImageView back_button = findViewById(R.id.history_back_button);
        back_button.setOnClickListener(view -> Coin_flip_history.super.onBackPressed());
    }
    private void setup_history_list() {
        coin_list = getIntent().getParcelableArrayListExtra("COIN_FLIP_LIST");
        if(!coin_list.isEmpty()) {
            TextView info = findViewById(R.id.history_info);
            info.setText("");
        }
        ArrayAdapter<Coin_Flip> adapter = new Coin_Flip_Adapter();
        ListView list = findViewById(R.id.coin_flip_list);
        list.setAdapter(adapter);

    }

    private class Coin_Flip_Adapter extends ArrayAdapter<Coin_Flip>{
        public Coin_Flip_Adapter() {
            super(Coin_flip_history.this,
                    R.layout.flip_history_list,
                    coin_list);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //Make sure we have a view to work with (could be null)
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.flip_history_list,parent,false);
            }
            //populate the list
            //get current coin_flip
            Coin_Flip current_coin_flip = coin_list.get(position);

            //fill view

            CardView cardView = itemView.findViewById(R.id.history_list_card_view);
            int width = cardView.getLayoutParams().width;
            cardView.setRadius((float) width/2);

            ImageView profile = itemView.findViewById(R.id.history_list_profile);
            if(current_coin_flip.getImage().equals("Default pic")){
                profile.setImageResource(R.drawable.default_profile);
            } else {
                loadImageFromStorage(current_coin_flip.getImage(),current_coin_flip.getName(),profile);
            }


            ImageView imageView = itemView.findViewById(R.id.coin_flip_image);
            if(current_coin_flip.getResult()) {
                imageView.setImageResource(R.drawable.check_mark);
            }
            else {
                imageView.setImageResource(R.drawable.clear_mark);
            }

            TextView timeView = itemView.findViewById(R.id.history_time);
            timeView.setText(current_coin_flip.getTime());

            TextView nameView = itemView.findViewById(R.id.history_list_name);
            nameView.setText(current_coin_flip.getName());

            return itemView;
        }
    }

    //pass in filename (which is child's name) and the image path to load image.
    private void loadImageFromStorage(String path, String filename, ImageView imageView)
    {

        try {
            File f=new File(path, filename + ".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}