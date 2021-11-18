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

    public ArrayList<Task> getTask_list() {
        return task_list;
    }
}
