package cmpt276.groupassignment.parentapp.model;

import java.util.ArrayList;
import java.util.List;

public class Coin_Flip_Manager {
    private ArrayList<ArrayList<String>> coin_flip_list;
    private static Coin_Flip_Manager instance;
    public Coin_Flip_Manager() {

        this.coin_flip_list = new ArrayList<>();
    }

    public static Coin_Flip_Manager getInstance(){
        if(instance == null){
            instance = new Coin_Flip_Manager();
        }
        return instance;
    }

    public void add_flip(Coin_Flip flip){
        coin_flip_list.add(flip.get_list());
    }

    public ArrayList<ArrayList<String>> getCoin_flip_list() {
        return coin_flip_list;
    }
}
