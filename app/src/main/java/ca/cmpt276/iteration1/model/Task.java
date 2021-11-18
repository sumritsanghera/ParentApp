package ca.cmpt276.iteration1.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Task implements Parcelable {
    ArrayList<String> queue;
    String task_description;

    public Task(ArrayList<String> queue, String task_description) {
        this.queue = queue;
        this.task_description = task_description;
    }

    public ArrayList<String> getQueue() {
        return queue;
    }

    public String getTask_description() {
        return task_description;
    }

    protected Task(Parcel in) {
        queue = in.createStringArrayList();
        task_description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(queue);
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
}
