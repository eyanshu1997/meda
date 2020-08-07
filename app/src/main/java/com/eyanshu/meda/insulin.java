package com.eyanshu.meda;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class insulin {
   // String user;
   FirebaseAuth mAuth;

    FirebaseUser user;
    public insulin(){}
    public insulin( String value) {
        mAuth=FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        //this.user = user;
        Value = Integer.parseInt(value);
        Date currentTime = Calendar.getInstance().getTime();
        time=currentTime.toString();
    }
    public void upload()
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(user.getUid()).child("insulin").push();
        ref.setValue(this);
    }
    String time;
    int Value;

    //public String getUser() {
    //    return user;
   // }

    //public void setUser(String user) {
       // this.user = user;
   // }

    @Override
    public String toString() {
        return "insulin{" +
                "time='" + time + '\'' +
                ", Value=" + Value +
                '}';
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
