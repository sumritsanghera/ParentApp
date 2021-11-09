package ca.cmpt276.iteration1.model;

import java.util.ArrayList;

/*
    Coin Flip class:
    -   Stores name of picker, their coin flip time and the result of their flip.
    -   To be added into the coin flip manager.
 */

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
