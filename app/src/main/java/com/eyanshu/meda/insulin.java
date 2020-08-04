package com.eyanshu.meda;

import java.util.Calendar;
import java.util.Date;

public class insulin {
    String user;

    public insulin(String user, int value) {
        this.user = user;
        Value = value;
        Date currentTime = Calendar.getInstance().getTime();
        time=currentTime.toString();
    }

    String time;
    int Value;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }

}
