package ca.cmpt276.ParentApp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/*
    Task object stores a single task with a children queue and a task description.
    -   Store functions to add/remove/update children queue
 */

public class Task implements Parcelable {
    private final ArrayList<Child> queue;
    private final String task_description;

    public Task(ArrayList<Child> queue, String task_description) {
        this.queue = queue;
        this.task_description = task_description;
    }

    protected Task(Parcel in) {
        queue = in.createTypedArrayList(Child.CREATOR);
        task_description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(queue);
        dest.writeString(task_description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public ArrayList<Child> getQueue() {
        return queue;
    }

    public String getTask_description() {
        return task_description;
    }

    public String getName(){
        return queue.get(0).getName();
    }
    public String getImage() { return queue.get(0).getImagePath(); }

    public void remove(int i){
        queue.remove(i);
    }

    public void add(Child child){
        queue.add(child);
    }

    public void update_queue(){
        Child child = queue.get(0);
        queue.remove(0);
        queue.add(child);
    }
}
