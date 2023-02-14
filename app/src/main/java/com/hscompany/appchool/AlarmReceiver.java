package com.hscompany.appchool;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    static final String TAG = AlarmReceiver.class.getSimpleName();
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        String action = intent.getAction();

        Log.i(TAG, "알람!");

        if (action.equals("com.gugu.alarm_message")) {  // 진짜 알람일때
            Log.i(TAG, "알람이 발생되었습니다.");
            String title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            String index = intent.getStringExtra("index");

            Calendar calendar = Calendar.getInstance();
            int week = calendar.get(Calendar.DAY_OF_WEEK);

            String currentWeek = getWeek(week);
            boolean isMatchWeek = index.contains(currentWeek);

            if (isMatchWeek) {
                notificationSomething(title, content);
            }
        }


    }

    private String getWeek(int week) {
        String realWeek = "";
        switch (week) {

            case 1: // 일은 1~ 토는 7
                realWeek = "일";
                break;

            case 2:
                realWeek = "월";
                break;

            case 3:
                realWeek = "화";
                break;

            case 4:
                realWeek = "수";
                break;

            case 5:
                realWeek = "목";
                break;

            case 6:
                realWeek = "금";
                break;

            case 7:
                realWeek = "토";
                break;
        }
        return realWeek;
    }

    public void notificationSomething(String title, String content) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("notificationId", 1); //전달할 값
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "2")
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.mario)) //BitMap 이미지 요구
                .setContentTitle(title)  // 푸시알림 제목
                .setContentText(content)  // 푸시알림 내용
                // 더 많은 내용이라서 일부만 보여줘야 하는 경우 아래 주석을 제거하면 setContentText에 있는 문자열 대신 아래 문자열을 보여줌
                //.setStyle(new NotificationCompat.BigTextStyle().bigText("더 많은 내용을 보여줘야 하는 경우..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
                .setAutoCancel(true);

        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName = "노티페케이션 채널";
            String description = "오레오 이상을 위한 것임";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("2", channelName, importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        } else
            builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

        assert notificationManager != null;
        notificationManager.notify(1234, builder.build()); // 고유숫자로 노티피케이션 동작시킴

    }
}
