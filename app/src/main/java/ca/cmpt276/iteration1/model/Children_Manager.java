package ca.cmpt276.iteration1.model;

import java.util.ArrayList;

public class Children_Manager {
    private static Children_Manager instance;
    private ArrayList<String> children_list;
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
        children_list.add(child.getName());
    }

    public void clear(){
        children_list.clear();
    }

    public ArrayList<String> getChildren_list(){
        return children_list;
    }
}
