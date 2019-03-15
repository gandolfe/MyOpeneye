package yys.com.myopeneye.utils;

/**
 * Created by yangys on 2019/3/15.
 */

public class Util {

    public static String getTimeString(long duration){
        String durationstr = "";
        int seconds = (int) (duration % 60);
        int minutes = (int) (duration / 60);
        durationstr = (minutes>9? ""+minutes : "0"+minutes) +"'" + (seconds>9? ""+seconds : "0"+seconds +'"');
        return durationstr;
    };
}
