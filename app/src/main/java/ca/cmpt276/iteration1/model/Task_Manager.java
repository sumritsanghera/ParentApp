package ca.cmpt276.iteration1.model;

import java.util.ArrayList;

public class Task_Manager {
    public static Task_Manager instance;
    ArrayList<Task> task_list;

    public static Task_Manager getInstance(){
        if(instance == null){
            instance = new Task_Manager();
        }
        return instance;
    }

    public Task_Manager() {
       task_list = new ArrayList<>();
    }

    public void add_task(Task task){
        task_list.add(task);
    }

    public void clear(){
        task_list.clear();
    }

    public ArrayList<Task> getTask_list() {
        return task_list;
    }

    public void update_child_name_after_edit(ArrayList<Edited_Child> name_list){
        for(Task task : task_list){
            for(Child queue_child : task.getQueue()){
                for(Edited_Child edited_child : name_list){
                    if(queue_child.getName().equals(edited_child.getOriginal_name())){
                        queue_child.setName(edited_child.getNew_name());
                    }
                }
            }
        }
    }
}
