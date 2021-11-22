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
    private final String original_image;
    private final String new_image;

    public Edited_Child(String original_name,String new_name, String original_image, String new_image) {
        this.original_name = original_name;
        this.new_name = new_name;
        this.original_image = original_image;
        this.new_image = new_image;
;    }

    protected Edited_Child(Parcel in) {
        original_name = in.readString();
        new_name = in.readString();
        original_image = in.readString();
        new_image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_name);
        dest.writeString(new_name);
        dest.writeString(original_image);
        dest.writeString(new_image);
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

    public String getOriginal_image() {
        return original_image;
    }

    public String getNew_image() {
        return new_image;
    }
}
