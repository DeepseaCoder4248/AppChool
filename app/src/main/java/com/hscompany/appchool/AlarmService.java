package com.hscompany.appchool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class AlarmService extends Service {

    static final String TAG = AlarmService.class.getSimpleName();

    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // SharedPreference에 저장된 알람 리스트 가져옴

        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);
        // 1: 일요일
        // 2: 월요일
        // 3: 화요일
        // 4: 수요일
        // 5: 목요일
        // 6: 금요일
        // 7: 토요일

        SharedPreferences preferences = getSharedPreferences("ALARM", MODE_PRIVATE);

        Map<String, ?> keys = preferences.getAll();
        ArrayList<String> realKeys = new ArrayList<>();

        // SharedPreference의 키값들 수집
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            realKeys.add(entry.getKey());
        }


        // 각 key에 해당되는 알람 등록
        for (int index = 0; index < realKeys.size(); index++) {
            String data = preferences.getString(realKeys.get(index), "no");

            try {
                JSONObject root = new JSONObject(data);

                int hour = root.getInt("hour");
                int minute = root.getInt("minute");
                String title = root.getString("title");
                String content = root.getString("content");
                String indexes = root.getString("indexes");

                Log.i("gugu", "hour: " + hour);
                Log.i("gugu", "minute: " + minute);
                Log.i("gugu", "title: " + title);
                Log.i("gugu", "content: " + content);
                Log.i("gugu", "indexes: " + indexes);


                String[] days = indexes.split("/");
                int[] dayCodes = new int[days.length];

                for(int i=0; i< days.length; i++) {
                    dayCodes[i] = getDayCode(days[i]);
                }

                // 현재 요일과 일치하는 값만 setAlarm해주기
                // 요일 선택이 잘안됐던것
                for(int i=0; i<dayCodes.length; i++) {
                    if(currentDay == dayCodes[i]) {
                        // set Alarm
                        Log.i("gugu", "여기 오나?");
                        setAlarm(hour, minute, title, content);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        resetService();

        return START_STICKY;
    }

    public int getDayCode(String day) {
        switch (day) {
            case "sun":
                return 1;
            case "mon":
                return 2;
            case "tues":
                return 3;
            case "weds":
                return 4;
            case "thurs":
                return 5;
            case "fri":
                return 6;
            case "sat":
                return 7;
            default:
                return -1;
        }
    }


    // 하루마다 등록하는 메소드
    public void resetService() {
        Calendar cal = Calendar.getInstance();
        long time = (cal.getTimeInMillis() + (1000 * 60 * 60 * 24));

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        // 서비스 등록하라는 알람이 발생되면 액션명은 아래와 같음
        intent.setAction("com.gugu.register_service");
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // API 23 이전
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            } else {
            }
        } else {
            // API 23이후
            // shared에서 시,분,초

            Log.i(TAG, "time: " + time);

            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, sender);
        }

    }


    public void setAlarm(int hour, int minute, String title, String content) {
        // Intent, PendingIntent, AlarmManager
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        // 알람이 발생되는 액션명은 아래와 같음
        intent.setAction("com.gugu.alarm_message");
        intent.putExtra("title", title);
        intent.putExtra("content", content);

        Log.i("gugu", "1");

        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // 안드로이드 버전에 따라서 동작방식이 다르다
        // 23이전과 이후로 (나눠야함)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // API 23 이전
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            } else {
            }
        } else {
            // API 23이후
            // shared에서 시,분,초

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, 0);

            long after = cal.getTimeInMillis();

            Log.i(TAG, "after: " + after);

            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, after, sender);
        }

    }
}