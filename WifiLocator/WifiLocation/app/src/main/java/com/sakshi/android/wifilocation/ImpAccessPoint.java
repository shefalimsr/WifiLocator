package com.sakshi.android.wifilocation;

/**
 * Created by shivanshsharma on 26/12/17.
 */

public class ImpAccessPoint {


    String day;
    int ID;
    String mac;
    float max_lev;
    long  room;




    public ImpAccessPoint()
    {

    }


    public ImpAccessPoint(int i , String d ,String m,float m_level ,long  r)
    {

    ID = i;
    max_lev=m_level;
        room=r;
        mac=m;

        day =d;

    }



    public long  getRoom()
    {
        return room;
    }
    public String getMac()
    {
        return mac;
    }
    public String getDay(){return day;}

}
