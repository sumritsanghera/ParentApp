package ca.cmpt276.ParentApp.model;

import java.util.ArrayList;

/*
    Coin Flip Manager:
    -   Manages coin flips, saves ArrayList of ArrayList of Strings to store name/time/result for
        each flip.
    -   Creates static instance of class, is implemented in main menu.
 */

public class Coin_Flip_Manager {
    private final ArrayList<Coin_Flip> coin_flip_list;
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

    public void update_child_after_edit(ArrayList<Edited_Child> name_list){
        for(Coin_Flip flip : coin_flip_list){
            for(Edited_Child child : name_list){
                if(flip.getName().equals(child.getOriginal_name()) &&
                        flip.getImage().equals(child.getOriginal_image())){
                    flip.setName(child.getNew_name());
                    flip.setImage(child.getNew_image());
                }
            }
        }
    }

    public void remove_child(ArrayList<Child> removed_list) {
        ArrayList<Integer> indices = new ArrayList<>();
        int index=0;
        if(!removed_list.isEmpty() && !coin_flip_list.isEmpty()) {
            for (Coin_Flip flip : coin_flip_list) {
                for (Child removed_child : removed_list) {
                    if (flip.getName().equals(removed_child.getName()) &&
                        flip.getImage().equals(removed_child.getImagePath())) {
                        indices.add(index);
                    }
                }
                index++;
            }
        }
        if(indices.size()>0){
            for(int i = indices.size()-1; i > -1; i--){
                coin_flip_list.remove((int)indices.get(i));
            }
        }

    }
}
