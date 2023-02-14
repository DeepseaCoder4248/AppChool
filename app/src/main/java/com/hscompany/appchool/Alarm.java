package com.hscompany.appchool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

// alram code -> FCM, Notification
// Calendar
// Advertisement

public class Alarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "알람~!!", Toast.LENGTH_SHORT).show();    // AVD 확인용
        Log.e("Alarm","알람입니다.");    // 로그 확인용

        // notification
        // title, content, image
//        String title = intent.getStringExtra("title");
//        String content = intent.getStringExtra("content");
//
//        Log.i("Alram", "title: " + title);
//        Log.i("Alram", "content: " + content);
    }
}
