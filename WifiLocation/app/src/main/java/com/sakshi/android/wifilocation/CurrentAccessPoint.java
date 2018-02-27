package com.sakshi.android.wifilocation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shivanshsharma on 25/12/17.
 */

public class CurrentAccessPoint implements Parcelable{

    String mac;
    float level;
    String time;



    public CurrentAccessPoint()
    {

    }


    public CurrentAccessPoint(String m, float l,String t)
    {

        mac=m;
        level=l;
        time=t;

    }


    protected CurrentAccessPoint(Parcel in) {
        mac = in.readString();
        level = in.readFloat();
        time = in.readString();
    }

    public static final Creator<CurrentAccessPoint> CREATOR = new Creator<CurrentAccessPoint>() {
        @Override
        public CurrentAccessPoint createFromParcel(Parcel in) {
            return new CurrentAccessPoint(in);
        }

        @Override
        public CurrentAccessPoint[] newArray(int size) {
            return new CurrentAccessPoint[size];
        }
    };

    public String getMac()
    {
        return mac;
    }

    public float getLevel()
    {
        return level;
    }

    public String getTime()
    {
        return time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mac);
        parcel.writeFloat(level);
        parcel.writeString(time);
    }
}

