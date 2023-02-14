package com.hscompany.appchool;

import android.app.AlarmManager;
import android.content.Context;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Global {
    static boolean dialogFlag = false; // 깃발?
    static int isAppFirst = -1;
    static AlarmManager alarmManager;
    static Context context;
    static FragmentManager fragmentManager;
    static boolean isMainFirst = false;

}
