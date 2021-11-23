package ca.cmpt276.ParentApp.model;

import java.util.ArrayList;

/*
    Task Manager contains list of all Tasks
    - Manages tasks such as add/remove/update after name edit/remove/add
 */

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

    public void update_child_after_edit(ArrayList<Edited_Child> name_list){
        for(Task task : task_list){
            for(Child queue_child : task.getQueue()){
                for(Edited_Child edited_child : name_list){
                    if(queue_child.getName().equals(edited_child.getOriginal_name()) &&
                        queue_child.getImagePath().equals(edited_child.getOriginal_image())){
                        queue_child.setName(edited_child.getNew_name());
                        queue_child.setImage(edited_child.getNew_image());
                    }
                }
            }
        }
    }

    public void addUpdate(ArrayList<Child> added_list){
        for(Task task: task_list){
            for(Child added_child: added_list){
                task.add(added_child);
            }
        }
    }

    public void removeUpdate(ArrayList<Child> removed_list) {
        for(Task task: task_list){
            ArrayList<Integer> indices = new ArrayList<>();
            int index=0;
            if(!removed_list.isEmpty() && !task.getQueue().isEmpty()) {
                for (Child child : task.getQueue()) {
                    for (Child removed_child : removed_list) {
                        if (child.getName().equals(removed_child.getName()) &&
                                child.getImagePath().equals(removed_child.getImagePath())) {
                            indices.add(index);
                        }
                    }
                    index++;
                }
            }
            if(indices.size()>0){
                for(int i = indices.size()-1; i > -1; i--){
                    task.remove(indices.get(i));
                }
            }

        }
    }

}
