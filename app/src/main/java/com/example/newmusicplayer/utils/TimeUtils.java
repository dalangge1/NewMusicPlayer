package com.example.newmusicplayer.utils;

public class TimeUtils {
    public static String getGapTime(int time) {
        time %= 60*60*1000;
        int min = time/60/1000;
        time %= 60*1000;
        int sec = time/1000;
        StringBuffer s = new StringBuffer();
        if (min<10){
            s.append("0").append(min);
        }else {
            s.append(min);
        }
        s.append(":");
        if (sec<10){
            s.append("0").append(sec);
        }else {
            s.append(sec);
        }
        return s.toString();
    }
}