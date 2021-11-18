package ca.cmpt276.iteration1.model;

import android.os.Parcel;
import android.os.Parcelable;

/*
    Coin Flip class:
    -   Stores name of picker, their coin flip time and the result of their flip.
    -   To be added into the coin flip manager.
    -   Implements Parcelable for easy data send between activities.
 */

public class Coin_Flip implements Parcelable {
    String name;
    Boolean result;
    String time;

    public Coin_Flip(String name, boolean result, String time) {
        this.name = name;
        this.result = result;
        this.time = time;

    }

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

    protected Coin_Flip(Parcel in) {
        name = in.readString();
        byte tmpResult = in.readByte();
        result = tmpResult == 0 ? null : tmpResult == 1;
        time = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
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
}
