package ca.cmpt276.iteration1;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/*
    Coin Flip History:
    -   Lists all coin flips, with their flip time, picker's name and result.
    -   Flip animation starts when tapped on coin
    -   Result is displayed as check mark or clear mark, depending if result match picker's guess.

 */

public class Coin_flip_history extends AppCompatActivity {

    private ArrayAdapter adapter;
    private ArrayList<ArrayList<String>> coin_list;

    @Override
    protected void onResume() {
        super.onResume();
        setup_history_list();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_history);
        setSupportActionBar(findViewById(R.id.coin_flip_menu_toolbar));
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        setup_history_list();
    }

    private void setup_history_list() {

        coin_list = (ArrayList<ArrayList<String>>) getIntent().getSerializableExtra("COIN_FLIP_LIST");
        if(!coin_list.isEmpty()) {
            TextView info = findViewById(R.id.history_info);
            info.setText("");
        }
        adapter = new Coin_Flip_Adapter();
        ListView list = findViewById(R.id.coin_flip_list);
        list.setAdapter(adapter);

    }

    private class Coin_Flip_Adapter extends ArrayAdapter<ArrayList<String>>{
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
            ArrayList<String> current_coin_flip = coin_list.get(position);

            //fill view
            ImageView imageView = itemView.findViewById(R.id.coin_flip_image);
            if(Boolean.parseBoolean(current_coin_flip.get(1)))
                imageView.setImageDrawable(getDrawable(R.drawable.check_mark));
            else
                imageView.setImageDrawable(getDrawable(R.drawable.clear_mark));

            TextView timeView = itemView.findViewById(R.id.date_time_flip);
            timeView.setText(current_coin_flip.get(2));

            TextView nameView = itemView.findViewById(R.id.name);
            nameView.setText(current_coin_flip.get(0));

            return itemView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}