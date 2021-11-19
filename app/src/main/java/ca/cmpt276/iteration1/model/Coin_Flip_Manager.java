package ca.cmpt276.iteration1.model;

import java.util.ArrayList;

/*
    Coin Flip Manager:
    -   Manages coin flips, saves ArrayList of ArrayList of Strings to store name/time/result for
        each flip.
    -   Creates static instance of class, is implemented in main menu.
 */

public class Coin_Flip_Manager {
    private ArrayList<Coin_Flip> coin_flip_list;
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
        coin_flip_list.add(flip);
    }

    public ArrayList<Coin_Flip> getCoin_flip_list() {
        return coin_flip_list;
    }

    public void update_child_name_after_edit(ArrayList<Edited_Child> name_list){
        for(Coin_Flip flip : coin_flip_list){
            for(Edited_Child child : name_list){
                if(flip.getName().equals(child.getOriginal_name())){
                    flip.setName(child.getNew_name());
                }
            }
        }
    }

    public void remove_child(ArrayList<Child> removed_list) {
        for(Coin_Flip flip : coin_flip_list){
            for(Child child : removed_list){
                if(flip.getName().equals(child.getName())){
                    coin_flip_list.remove(flip);
                }
            }
        }
    }
}
