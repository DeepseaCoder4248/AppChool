package com.hscompany.appchool;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {

    ImageView splashImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        // 하루마다 알람리스트 감지하는 서비스 최초 실행
//        Calendar cal = Calendar.getInstance();
//        long time = (cal.getTimeInMillis() + (1000 * 10));
//
//        // Intent, PendingIntent, AlarmManager
//        Intent intent = new Intent(this, AlarmReceiver.class);
//
//        intent.setAction("com.gugu.register_service");
//
//        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
//        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            // API 23 이전
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            } else {
//
//            }
//        } else {
//
//            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, sender);
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // introduce switch
                if (introdueSwitch() == 0) { // 0이면 introduce로
                    Intent intent = new Intent(SplashActivity.this, IntroduceActivity.class);
                    startActivity(intent);
                    finish();

                } else if (introdueSwitch() == 1) { // 1이면 LoginActivity로
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, 3000);
    }

    // introduceActivity의 실행여부. 0이면 최초, 1이면 다음에는 표시 x
    public int introdueSwitch() {

        // sharedFreferences 불러오기.
        SharedPreferences sharedPreferences = getSharedPreferences("INTRODUCE_SWITCH", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int status = sharedPreferences.getInt("status", 0); // 불러오기, 최초라면 0임.

        Global.isAppFirst = status;

        // 불러올 때 기본값이 0이라면, 추가시키기.
        if (status == 0) {
            editor.putInt("status", 1);
            editor.commit();
        }

        return status;
    }
}