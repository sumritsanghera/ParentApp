package ca.cmpt276.iteration1.model;

import java.util.ArrayList;

public class Coin_Flip {
    private ArrayList<String> list;

    public Coin_Flip(String name, boolean result, String time) {
        list = new ArrayList<>();
        list.add(name);
        list.add(String.valueOf(result));
        list.add(time);

    }

    public ArrayList<String> get_list(){
        return list;
    }
}
