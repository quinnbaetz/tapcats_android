package com.mi.blockslide;

import android.util.Log;

/**
 * Created by quinn on 7/28/14.
 */
public class Logger {
    public static void Log(Exception e) {
        if(e != null)
            e.printStackTrace();
    }

    public static void Log(String message) {
        String tag = "";

        String exception = Log.getStackTraceString(new Exception());

        int start = exception.indexOf("(", exception.indexOf("(")+1);
        int stop = exception.indexOf(")", start + 1);
        if(start > 0 && stop > start){
            String line = exception.substring(start, stop + 1);

            if(line.length() > 1 && line.indexOf(":") > 1) {
                String file = line.substring(1, line.indexOf(":"));
                if (tag == null || tag.isEmpty()) {
                    tag = file;
                }
                if (message != null)
                    Log.e(tag, message + " " + exception.substring(start, stop + 1));
            }
        }
    }


    public static void Log(String message, Exception e) {
        String tag = "";
        if (message != null)
            Log.e("exceptionHelp", message, e);
    }
}
