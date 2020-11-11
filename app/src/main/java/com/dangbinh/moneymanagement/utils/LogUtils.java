package com.dangbinh.moneymanagement.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by dangbinh on 9/11/2020.
 */
public class LogUtils {

    private static StringBuffer sStringBuffer = new StringBuffer();


    public interface LogListener {
        void onLogged(StringBuffer log);
    }

    private static LogListener sLogListener;

    public static void log(String tag, String message) {
        Log.d(tag, message);
        StringBuilder stringBuilder = new StringBuilder();
        String date = formatDate(Calendar.getInstance());
        stringBuilder.append(date);
        stringBuilder.append(" ");
        stringBuilder.append(tag);
        stringBuilder.append(" ");
        stringBuilder.append(message);
        stringBuilder.append("\n\n");
        sStringBuffer.insert(0, stringBuilder.toString());
        printLogs();
    }

    private static String formatDate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd hh:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
    }

    private static void printLogs() {
        if(sLogListener != null) {
            sLogListener.onLogged(sStringBuffer);
        }
    }

    public static void clearLogs() {
        sStringBuffer = new StringBuffer();
        printLogs();
    }

    public static void setLogListener(LogListener logListener) {
        sLogListener = logListener;
    }
}
