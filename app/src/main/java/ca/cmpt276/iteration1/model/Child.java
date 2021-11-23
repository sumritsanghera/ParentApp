package ca.cmpt276.iteration1.model;

/*
    Child class:
    -   Stores name of child to be added to a children manager in main menu.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Child implements Parcelable {
    private String name;
    private String bitmap;

    //Only use this constructor for when adding the no name person in queue.
    public Child(String name){
        this.name = name;
        this.bitmap = "Default pic";
    }

    public Child(String name, String bitmap) {
        this.name = name;
        this.bitmap = bitmap;
    }


    protected Child(Parcel in) {
        name = in.readString();
        bitmap = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(bitmap);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String name) {
        this.bitmap = name;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return bitmap;
    }

}


