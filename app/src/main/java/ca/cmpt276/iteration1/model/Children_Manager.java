package ca.cmpt276.iteration1.model;

import java.util.ArrayList;

/*
    Children Manager:
    -   Creates a static instance of children manager to add names of children and store
        in the main menu to be used to features: coin flip, coin flip history and
        configuration.
    -   Stores an ArrayList<String> of names.
 */

public class Children_Manager {
    private static Children_Manager instance;
    private ArrayList<Child> children_list;
    public Children_Manager() {
        this.children_list = new ArrayList<>();
    }


    public static Children_Manager getInstance(){
        if(instance == null){
            instance = new Children_Manager();
        }
        return instance;
    }

    public void addChild(Child child){
        children_list.add(child);
    }

    public void clear(){
        children_list.clear();
    }

    public int find_name(String name){
        for(Child child : children_list){
            if(child.getName().equals(name)){
                return children_list.indexOf(child);
            }
        }
        return -1;
    }

    public ArrayList<Child> getChildren_list(){
        return children_list;
    }
}
