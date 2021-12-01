package ca.cmpt276.ParentApp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task_History_Item implements Parcelable {
    private final String name;
    private final String image;
    private final String time;

    public Task_History_Item(String name, String image) {
        this.name = name;
        this.image = image;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd - HH:mma");
        LocalDateTime new_time = LocalDateTime.now();
        time = new_time.format(format);
    }

    protected Task_History_Item(Parcel in) {
        name = in.readString();
        image = in.readString();
        time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Task_History_Item> CREATOR = new Creator<Task_History_Item>() {
        @Override
        public Task_History_Item createFromParcel(Parcel in) {
            return new Task_History_Item(in);
        }

        @Override
        public Task_History_Item[] newArray(int size) {
            return new Task_History_Item[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }
}
