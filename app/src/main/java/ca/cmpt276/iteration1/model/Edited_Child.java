package ca.cmpt276.iteration1.model;

import android.os.Parcel;
import android.os.Parcelable;

/*
    Edited Child saves original and new name of child to be edited
    - passed array of Edited Child to other activities to update queues and lists
 */

public class Edited_Child implements Parcelable {
    private final String original_name;
    private final String new_name;

    public Edited_Child(String original_name,String new_name) {
        this.original_name = original_name;
        this.new_name = new_name;
    }

    protected Edited_Child(Parcel in) {
        original_name = in.readString();
        new_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_name);
        dest.writeString(new_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Edited_Child> CREATOR = new Creator<Edited_Child>() {
        @Override
        public Edited_Child createFromParcel(Parcel in) {
            return new Edited_Child(in);
        }

        @Override
        public Edited_Child[] newArray(int size) {
            return new Edited_Child[size];
        }
    };

    public String getOriginal_name() {
        return original_name;
    }

    public String getNew_name() {
        return new_name;
    }
}
