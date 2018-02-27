package com.sakshi.android.wifilocation;

/**
 * Created by shivanshsharma on 25/12/17.
 */

public class ImportantAccessPoint {
    int ID;
    String day;
    int hour;
    String mac;
    float max_level;
    long  room ;




    public ImportantAccessPoint()
    {

    }


    public ImportantAccessPoint( String d , int h ,int i , String m , float m_level ,long  r)
    {

        ID =i;
        room=r;
        mac=m;
        hour =h;
        day =d;
        max_level=m_level;

    }

public int getID(){
        return ID;
}
public float getMax_level(){
    return max_level;
    }

    public long  getRoom()
    {
        return room;
    }

    public String getMac()
    {
        return mac;
    }
    public int getHourOfDay(){return  hour;}
    public String getDay(){return day;}

}
