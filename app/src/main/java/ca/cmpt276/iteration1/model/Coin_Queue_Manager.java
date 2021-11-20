package ca.cmpt276.iteration1.model;

import java.util.ArrayList;

/*
    Coin Queue Manager saves queue of coin flips
        - Has function to dequeue (take first child and move to end of list)
        - Has function to iterate over queue and update edited names

 */

public class Coin_Queue_Manager {
    private ArrayList<Child> queue;
    private static Coin_Queue_Manager instance;

    public Coin_Queue_Manager() {
        queue = new ArrayList<>();
    }

    public static Coin_Queue_Manager getInstance(){
        if(instance == null){
            instance = new Coin_Queue_Manager();
        }
        return instance;
    }

    public void setQueue(ArrayList<Child>list){
        queue = list;
    }

    public void add(Child child){
        queue.add(child);
    }

    public void remove(int index){
        queue.remove(index);
    }

    public void clear(){
        queue.clear();
    }

    public Child get(int index){
        return queue.get(index);
    }

    public ArrayList<Child> getQueue() {
        return queue;
    }


    public String firstChild(){
        return queue.get(0).getName();
    }

    public void dequeue(){
        if(queue.size()>1){
            Child child = queue.get(0);
            queue.remove(0);
            queue.add(child);
        }
    }

    public void update_child_name_after_edit(ArrayList<Edited_Child> name_list){
        for(Child child : queue){
            for(Edited_Child edited_child : name_list){
                if(child.getName().equals(edited_child.getOriginal_name())){
                    child.setName(edited_child.getNew_name());
                }
            }
        }
    }
}
