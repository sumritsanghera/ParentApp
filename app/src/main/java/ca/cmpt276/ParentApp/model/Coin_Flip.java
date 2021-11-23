package ca.cmpt276.ParentApp.model;

import android.os.Parcel;
import android.os.Parcelable;

/*
    Coin Flip class:
    -   Stores name of picker, their coin flip time and the result of their flip.
    -   To be added into the coin flip manager.
    -   Implements Parcelable for easy data send between activities.
 */

public class Coin_Flip implements Parcelable {
    private String name;
    private String image;
    private final Boolean result;
    private final String time;

    public Coin_Flip(Child child, boolean result, String time) {
        this.name = child.getName();
        this.image = child.getImagePath();
        this.result = result;
        this.time = time;

    }

    protected Coin_Flip(Parcel in) {
        name = in.readString();
        image = in.readString();
        byte tmpResult = in.readByte();
        result = tmpResult == 0 ? null : tmpResult == 1;
        time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
        dest.writeByte((byte) (result == null ? 0 : result ? 1 : 2));
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Coin_Flip> CREATOR = new Creator<Coin_Flip>() {
        @Override
        public Coin_Flip createFromParcel(Parcel in) {
            return new Coin_Flip(in);
        }

        @Override
        public Coin_Flip[] newArray(int size) {
            return new Coin_Flip[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Boolean getResult() {
        return result;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
