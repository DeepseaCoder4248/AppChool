package com.hscompany.appchool;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// C, python, java

// 첫 화면

public class MainMenuFragment extends Fragment {
    Context context;
    View view;

    TextView tvMainMenuPercent;
    ListView lvMainmenu;
    FirebaseFirestore firebaseFirestore;
    SharedPreferences sharedPreferences;

    String Id;

    ListHomeAdapter adapter;

    LinearLayout lyAdmob;
    AdView adView;

    Calendar currentCal;
    int currentDayOfWeek;
    String currentDay;

    String TAG = MainMenuFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getContext();

        // false안줬을때, removeView 어쩌구 에러가 나왔다
        view = inflater.inflate(R.layout.mainmenu_fragment, container, false);
        tvMainMenuPercent = view.findViewById(R.id.tv_home_percent);

        Global.context = context;
        Global.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        adView = view.findViewById(R.id.adview_mainfragment_admob);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


//        Shader shader = new LinearGradient(0,0,0,tvMainMenuPercent.getLineHeight(),
//                startColor, endColor, Shader.TileMode.REPEAT);

        // x,y는 그라디언트의 방향을 정하기 위함
        // 시작색상, 종료색상
        // 모드: REPEAT (반복O), CLAMP (반복X)

//        int textGradientColor[] = new int[2];
//        textGradientColor[0] = Color.parseColor("#307CFF");
//        textGradientColor[1] = Color.parseColor("#B5DFFF");
//        float[] pos = {50f, 0f};
//
//        Shader shader = new LinearGradient(0, -272, 0, tvMainMenuPercent.getLineHeight(), textGradientColor, pos, Shader.TileMode.CLAMP);
//        tvMainMenuPercent.getPaint().setShader(shader);
//
//        Shader shader = new LinearGradient(0, 0, 0, tvMainMenuPercent.getLineHeight(), Color.parseColor("#307CFF"), Color.parseColor("#FF001E"), null);
//        tvMainMenuPercent.getPaint().setShader(shader);

//        int[] color = {Color.BLUE, Color.CYAN};
//        float[] position = {0.1f, 0.1f};
//        Shader.TileMode tile_mode = Shader.TileMode.REPEAT; // or TileMode.REPEAT;
//        LinearGradient lin_grad = new LinearGradient(0, -20, 0, 20, color, position, tile_mode);
//        Shader shader_gradient = lin_grad;
//        tvMainMenuPercent.getPaint().setShader(shader_gradient);

//        int[] color = {Color.DKGRAY, Color.CYAN};
//        float[] posion = {0, 1};
//        Shader.TileMode tileMode = Shader.TileMode.MIRROR;
//        LinearGradient gradient = new LinearGradient(0, 0, 0, 25, color, posion, tileMode);
//        Shader shader = gradient;
//        tvMainMenuPercent.getPaint().setShader(shader);

//        Canvas canvas = new Canvas();
//        Paint pnt = new Paint();
//        pnt.setAntiAlias(true);
//        int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.WHITE};
//        float[] pos = {0.0f, 0.1f, 0.6f, 0.9f, 1.0f};
//        pnt.setShader(new LinearGradient(0, 0, 100, 0, Color.BLUE, Color.WHITE, Shader.TileMode.CLAMP));
//        canvas.drawRect(0, 0, 100, 100, pnt);
//        tvMainMenuPercent.getPaint().setShader(pnt.getShader());

//        int colors[] = {Color.parseColor("#FF001E"), Color.parseColor("#FFE500"), Color.parseColor("#307CFF")};
//        float positions[] = {0.2f, 0.5f, 0.2f}; // 1.Red,2.Orange,3.Blue
//        float width = (float) tvMainMenuPercent.getMaxWidth();
//        float height = (float) tvMainMenuPercent.getLineHeight();
//
//        Shader shader = new LinearGradient(0, 0, 0, height, colors, positions, Shader.TileMode.CLAMP);
//        tvMainMenuPercent.getPaint().setShader(shader);
        // 1번은 위, 2번은 아래
//        int colors[] = {Color.parseColor("#000000"), Color.parseColor("#B02788FF")};
//        float positions[] = {0.2f, 0.4f}; // 1번 색상과 2번 색상의 위치. 1번과 2번 사이의 길이가 길어지면 농도가 정해진다.
//        float width = (float) tvMainMenuPercent.getMaxWidth();
//        float height = (float) tvMainMenuPercent.getLineHeight();
//
//        // x는 각도를 결정, y는 높이를 결정.
//        Shader shader = new LinearGradient(0, 0, 0, height, colors, positions, Shader.TileMode.CLAMP);
//        tvMainMenuPercent.getPaint().setShader(shader);
//        // textView의 색상을 덮어씌우는 것으로 정확한 색상을 표현하려면 textView의 색상이 흰색이어야 한다.

        Shader shader = new LinearGradient(25, -125, -25, 125,
                Color.parseColor("#2cb2ff"), // up , 연한
                Color.parseColor("#338eff"), // down , 진한
                Shader.TileMode.MIRROR);
        tvMainMenuPercent.getPaint().setShader(shader);
        Log.i(TAG, tvMainMenuPercent.getLineHeight() + "");


        lvMainmenu = view.findViewById(R.id.lv_mainmenu);

        sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        Id = sharedPreferences.getString("id", "");

        firebaseFirestore = FirebaseFirestore.getInstance();

        // adadpater객체 생성
        adapter = new ListHomeAdapter();

        Log.i(TAG, "id: " + Id);

        try {
            firebaseFirestore.collection(Id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.i(TAG, "documnt: " + document.getId() + "data: " + document.getData());

                            String appAndWeb = (String) document.getData().get("appAndWeb");
                            String content, indexes, link, title, appPackage, appLink;
                            long hour, minute;
                            boolean setTouchStatus;
                            long checkTime;

                            // int는 4바이트
                            //  long 8바이트

                            hour = (long) document.getData().get("hour");
                            minute = (long) document.getData().get("minute");
                            content = (String) document.getData().get("content");
                            indexes = (String) document.getData().get("indexes");
                            link = (String) document.getData().get("link");
                            appLink = (String) document.getData().get("appLink");
                            title = (String) document.getData().get("title");
                            appPackage = (String) document.getData().get("package");
                            setTouchStatus = (boolean) document.getData().get("setTouchStatus");
                            checkTime = (long) document.getData().get("checkTime");


                            // 시간 초기화
                            Calendar resetCal = Calendar.getInstance();
                            resetCal.set(Calendar.HOUR, 0);
                            resetCal.set(Calendar.MINUTE, 0);
                            resetCal.set(Calendar.SECOND, 0);
                            long resetTime = resetCal.getTimeInMillis();


                            // 리셋할 시간이, 현재 시간보다 커야 리셋이 됨.
                            if (checkTime <= resetTime) {
                                setTouchStatus = true;

                                Log.i(TAG, "checktime: " + checkTime + ",resettime: " + resetTime);
                            }

                            // ItemData객체로 만들고
                            ItemData itemData = new ItemData(appAndWeb, title, link, appLink, indexes, appPackage, content, hour, minute, setTouchStatus);

                            Log.i(TAG, itemData.toString());

                            currentCal = Calendar.getInstance();
                            currentDayOfWeek = currentCal.get(Calendar.DAY_OF_WEEK);

                            // https://chobopark.tistory.com/111
                            // currentDay 숫자에 따라서 월부터 일까지 String으로 대입, DB에 저장된 요일은 모두 한글 String임.
                            switch (currentDayOfWeek) {

                                case 1: // 일은 1~ 토는 7
                                    currentDay = "일";
                                    break;

                                case 2:
                                    currentDay = "월";
                                    break;

                                case 3:
                                    currentDay = "화";
                                    break;

                                case 4:
                                    currentDay = "수";
                                    break;

                                case 5:
                                    currentDay = "목";
                                    break;

                                case 6:
                                    currentDay = "금";
                                    break;

                                case 7:
                                    currentDay = "토";
                                    break;

                            }

                            if (indexes.contains(currentDay)) {
                                Log.i("SIBAL", "목요일 있다!");
                                // 어댑터의 addItem메소드 호출
                                adapter.addItem(itemData);
                            }

                            // 앱이 처음 실행됐다면
                            // 1) 파이어베이스의 일정데이터를 불러옴
                            // 2) 불러온 값으로 알람 재등록

                            if (Global.isAppFirst == 0 && !Global.isMainFirst) {
                                Log.i(TAG, "isAppFirst: " + Global.isAppFirst);

                                regist(itemData.getIndexes(), (int) itemData.getHour(), (int) itemData.getMinute(), itemData.getTitle(), itemData.getContent());
                            }
                        }
                        Global.isMainFirst = true;

                        adapter.fragmentUseCancelLine(0); // 메인메뉴 프래그먼트에는 취소선 적용할 수 있도록 스위치

                        lvMainmenu.setAdapter(adapter);

                        int resultPercent = getCheckedPercent(adapter);

                        // 4) textview에 세팅
                        tvMainMenuPercent.setText(resultPercent + "%");


                        // 만약 resultPercent가 100이라면
                        // SharePreference에 현재 날짜를 넣어줌

                        SharedPreferences preferences = context.getSharedPreferences("CALENDAR", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();


                        // 오늘 출석률 100%라면
                        if (resultPercent == 100) {


                            // 1) 현재 날짜 가져옴
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-d");
                            String currentDay = simpleDateFormat.format(new Date(Calendar.getInstance().getTimeInMillis()));

                            // 2) Shared에 넣어주기
                            editor.putInt(currentDay, 1);
                            editor.commit();

                            // 키: 날짜
                            // 값: 1
//                        2021-11-04

                        }
                    }
                }
            });
        } catch(Exception e) {
            e.printStackTrace();
        }


        lvMainmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // 기본 정보 불러오기
                ItemData data = new ItemData(); // 객체를 실시간으로 초기화.
                data = adapter.setItem(i); // adapter에서 해당 position에 맞는 data를 리턴한다.
                boolean setTouchStatus = data.getSetTouchStatus(); // 출석완료를 의미하는 터치 가능여부.

                // setTouchStatus를 통해서 해당 아이템이 터치가 가능한지 불가능한지 확인.
                if (setTouchStatus == false) {
                    Toast.makeText(context, "오늘은 출석을 완료하였습니다.", Toast.LENGTH_SHORT).show();

                } else if (setTouchStatus == true) {
                    String appAndWeb = data.getAppAndWeb(); // data 안에 있는 App / Web에 따라서 App실행과 Web 실행이 결정된다.

                    // type이 app이면 외부 앱 실행을, web이면 브라우저를 실행.
                    if (appAndWeb.equals("web")) { // web
                        String link = data.getLink(); // 해당 사이트 링크 받아오기.
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link)); // uri intent 사용하기.
                        startActivity(webIntent);

                    } else if (appAndWeb.equals("app")) { // app
                        String appPackage = data.getAppPackage(); // AppPackage 가져오기.
                        Log.i(TAG, "package: " + appPackage);
//                        String appLink = data.getAppLink(); // AppLink 가져오기.
//                         Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appLink));
//                         Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(appPackage); // appPackage intent 사용하기.

                        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appPackage);
                        if (launchIntent != null) {
                            startActivity(launchIntent);//null pointer check in case package name was not found
                        }
                    }

                    Calendar calendar = Calendar.getInstance();
                    long checkTime = calendar.getTimeInMillis();

                    // 터치된 내용(MainMenu든 뭐든)은 터치가 불가능하게 하고, 터치가능 여부를 false로 한다.
                    firebaseFirestore.collection(Id).document(data.getTitle()).update("setTouchStatus", false); // 아이디 / 선택한 아이템의 제목 / 단일 데이터 추가.
                    firebaseFirestore.collection(Id).document(data.getTitle()).update("checkTime", checkTime);

                    // 핵심 맞음
                    adapter = new ListHomeAdapter(); // 해당 어댑터를 초기화 시켜서 모든 데이터를 초기화 하고 다시 불러온다.

                    // referesh fragment 사용하는가? 아니면 어떻게 하는가?
                    firebaseFirestore.collection(Id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.i(TAG, "documnt: " + document.getId() + "data: " + document.getData());

                                    String appAndWeb = (String) document.getData().get("appAndWeb");
                                    String content, indexes, link, appLink, title, appPackage;
                                    long hour, minute;
                                    boolean setTouchStatus;
                                    long checkTime;

                                    // int는 4바이트
                                    //  long 8바이트

                                    hour = (long) document.getData().get("hour");
                                    minute = (long) document.getData().get("minute");
                                    content = (String) document.getData().get("content");
                                    indexes = (String) document.getData().get("indexes");
                                    link = (String) document.getData().get("link");
                                    appLink = (String) document.getData().get("appLink");
                                    title = (String) document.getData().get("title");
                                    appPackage = (String) document.getData().get("package");
                                    setTouchStatus = (boolean) document.getData().get("setTouchStatus");
                                    checkTime = (long) document.getData().get("checkTime");

//                        Log.i("MMF_USER_GET", "content: " + content +
//                                "/indexes: " + indexes +
//                                "/link: " + link +
//                                "/title: " + title +
//                                "/appPackage: " + appPackage +
//                                "/year: " + year +
//                                "/month: " + month +
//                                "/day: " + day +
//                                "/hour: " + hour +
//                                "/minute: " + minute);

                                    // ItemData객체로 만들고
                                    ItemData itemData = new ItemData(appAndWeb, title, link, appLink, indexes, appPackage, content, hour, minute, setTouchStatus);

                                    Log.i(TAG, itemData.toString());

                                    currentCal = Calendar.getInstance();
                                    currentDayOfWeek = currentCal.get(Calendar.DAY_OF_WEEK);

                                    // https://chobopark.tistory.com/111
                                    // currentDay 숫자에 따라서 월부터 일까지 String으로 대입, DB에 저장된 요일은 모두 한글 String임.
                                    switch (currentDayOfWeek) {

                                        case 1: // 일은 1~ 토는 7
                                            currentDay = "일";
                                            break;

                                        case 2:
                                            currentDay = "월";
                                            break;

                                        case 3:
                                            currentDay = "화";
                                            break;

                                        case 4:
                                            currentDay = "수";
                                            break;

                                        case 5:
                                            currentDay = "목";
                                            break;

                                        case 6:
                                            currentDay = "금";
                                            break;

                                        case 7:
                                            currentDay = "토";
                                            break;

                                    }

                                    if (indexes.contains(currentDay)) {
                                        Log.i("SIBAL", "목요일 있다!");
                                        // 어댑터의 addItem메소드 호출
                                        adapter.addItem(itemData);
                                    }

                                    // 어댑터의 addItem메소드 호출
//                                    adapter.addItem(itemData);
                                }

                                lvMainmenu.setAdapter(adapter);

                                int resultPercent = getCheckedPercent(adapter);

                                // 4) textview에 세팅
                                tvMainMenuPercent.setText(resultPercent + "%");

                                // 만약 resultPercent가 100이라면
                                // SharePreference에 현재 날짜를 넣어줌

                                SharedPreferences preferences = context.getSharedPreferences("CALENDAR", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();

                                // 오늘 출석률 100%라면
                                if (resultPercent == 100) {

                                    // 1) 현재 날짜 가져옴
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-d");
                                    String currentDay = simpleDateFormat.format(new Date(Calendar.getInstance().getTimeInMillis()));

                                    // 2) Shared에 넣어주기
                                    editor.putInt(currentDay, 1);
                                    editor.commit();

                                }

                            }
                        }
                    });
                }
            }
        });
        // 출석률
        // 리스트뷰
        return view;
    }

    // 일정등록 -> 파이어베이스 저장 -> 알람등록
    public void regist(String index, int hour, int minute, String title, String content) {

        Log.i("gugu", "=========================================");
        Log.i("gugu", "알람등록");
        Log.i("gugu", "index: " + index);
        Log.i("gugu", "hour: " + hour);
        Log.i("gugu", "minute: " + minute);
        Log.i("gugu", "title: " + title);
        Log.i("gugu", "content: " + content);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("com.gugu.alarm_message");
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("index", index);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0,intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);



        Log.i("gugu", "=========================================");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 지정한 시간에 매일 알림
        // 안드로이드 5.0이상부터는 최소 반복주기 1분
        Global.alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  AlarmManager.INTERVAL_DAY, pIntent);

    }

    public void unregist() {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Global.alarmManager.cancel(pIntent);
    }

    public int getCheckedPercent(ListHomeAdapter getDataPercent) {
        // 1) 전체를 가져온다 -> 전체 개수를 가져온다. -> 등록된 일정의 전체 개수를 가져온다.
        int allPercent = getDataPercent.datas.size();

        // 2) setTouch가 false인 개수를 가져온다.
        int checkedCount = 0;

        // 1) datas를 전부 찾아봐야함
        // 2) datas에서 false인 객체를 알아야함
        // 3) false인지 확인후 카운팅
        for (int i = 0; i < adapter.datas.size(); i++) {
            ItemData item = adapter.datas.get(i);

            if (item.getSetTouchStatus() == false) {
                checkedCount++;
            }
        }

        // 3) 구한값으로 백분율구하기
        double result = ((double) checkedCount / allPercent) * 100;
        Log.i(TAG, "최종 퍼센트: " + result);

        int resultPercent = (int) result;
        Log.i(TAG, "두자리 수 최종 퍼센트: " + resultPercent);

        return resultPercent;
    }
}
