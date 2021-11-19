package ca.cmpt276.iteration1.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Task implements Parcelable {
    private ArrayList<Child> queue;
    private String task_description;

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

    public void setQueue(ArrayList<Child> queue) {
        this.queue = queue;
    }

    public String getTask_description() {
        return task_description;
    }

    public String getName(){
        if(queue.size()>0){
            return queue.get(0).getName();
        }
        return "";
    }

    public void update_queue(){
        if(queue.size() > 1){
            Child first = queue.get(0);
            queue.remove(0);
            queue.add(first);
        }
    }
}
