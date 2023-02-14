package com.hscompany.appchool;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RecycleDataDialog {

    EditText edtTitle; // 제목
    EditText edtText; // 추가할 내용
    EditText edtLink; // 앱(Package), 웹(Http)의 링크
    EditText edtSetTime; // 시간 설정하기
    ImageView ivbtnTime; // 시간 설정
    ImageView ivbtnAppApi; // 앱(Package) 불러오기
    ImageView ivbtnOk; // 확인
    ImageView ivbtnDelete;

    ImageView tvAddApp; // 앱으로 전환 버튼
    ImageView tvAddWeb; // 웹으로 전환 버튼

    boolean appAndWeb = false; // 앱인지 웹인지 데이터베이스에 저장해줌.
    String title;
    String content; //
    String link = "null";
    String packagee = "null";
    String appName;
    String appLink = "";
    int hour = 0;
    int minute = 0;
    String indexes;
    boolean setTouchStatus = true;

    Map<String, ?> alarmData = new HashMap<>();

    // 로그인 한 Id를 불러오기 위한 객체들
    CheckManagerFragment checkManagerFragment;
    String getId;

    BroadcastReceiver receiver;

    private Context context; // 다이어로그용

    private AlarmManager alarmManager;

    ItemData itemData;

    String TAG = RecycleDataDialog.class.getSimpleName();

    public RecycleDataDialog(Context context, CheckManagerFragment checkManagerFragment) {
        this.context = context;
        this.checkManagerFragment = checkManagerFragment;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_recycle_add_data);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        edtTitle = dlg.findViewById(R.id.edt_recycle_add_title);
        edtText = dlg.findViewById(R.id.edt_recycle_add_text);
        edtLink = dlg.findViewById(R.id.edt_recycle_add_link);
        edtSetTime = dlg.findViewById(R.id.edt_recycle_add_time);
        ivbtnTime = dlg.findViewById(R.id.ivbtn_recycle_add_time);
        ivbtnAppApi = dlg.findViewById(R.id.ivbtn_recycle_add_appApi);
        ivbtnOk = dlg.findViewById(R.id.ivbtn_recycle_add_ok);
        ivbtnDelete = dlg.findViewById(R.id.ivbtn_recycle_delete);

        tvAddApp = dlg.findViewById(R.id.tv_dialog_recycle_addApp);
        tvAddWeb = dlg.findViewById(R.id.tv_dialog_recycle_addweb);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 미선택시 기본값을 App으로 설정.
        tvAddApp.setImageResource(R.drawable.btnselectapp); // App을 칼라 처리
        tvAddApp.setVisibility(View.INVISIBLE); // App을 안보이게
        tvAddApp.setEnabled(false); // App 클릭 여부 불가능

        tvAddWeb.setImageResource(R.drawable.btnnoneweb); // Web을 흑백 처리
        tvAddWeb.setVisibility(View.INVISIBLE); // Web을 안보이게
        tvAddWeb.setEnabled(false); // Web 클릭 여부 불가능

        // ItemData의 데이터를 가져옴.
        String titleData = itemData.getTitle();
        String linkData = itemData.getLink();
        String appLinkData = itemData.getAppLink();
        String appAndWebData = itemData.getAppAndWeb();
        String indexesData = itemData.getIndexes();
        String appPackageData = itemData.getAppPackage();
        String contentData = itemData.getContent();
        long hourData = itemData.getHour();
        long minuteData = itemData.getMinute();
        boolean setTouchStatusData = itemData.getSetTouchStatus();
        boolean isApp = (appAndWebData.equals("app")) ? true : false;
        Log.i(TAG, "bool : " + isApp);

        if (isApp) {
            tvAddApp.setImageResource(R.drawable.btnselectapp); // App을 칼라 처리
            tvAddWeb.setImageResource(R.drawable.btnnoneweb); // Web을 흑백 처리

            ivbtnAppApi.setVisibility(View.VISIBLE); // AppPackmanager 실행 버튼.
            edtSetTime.setVisibility(View.GONE);

            edtLink.setEnabled(false); // 입력을 하지 못하게 하기.
            edtLink.setHint("하단의 앱 등록하기를 터치하세요");

            // 값 세팅
            edtTitle.setText(titleData);
            edtText.setText(contentData);
            edtLink.setText(linkData);
            hour = (int) hourData;
            minute = (int) minuteData;
            indexes = indexesData;
            packagee = appPackageData;
            setTouchStatus = setTouchStatusData;

            edtSetTime.setEnabled(false);
            edtSetTime.setVisibility(View.VISIBLE);
            edtSetTime.setTextColor(Color.parseColor("#FFB1B1B1")); // 앱으로 선택된 링크는 수정이 불가능하다는 의미로 회색처리.
            edtSetTime.setText(hour + "시 " + minute + "분 " + indexes + "요일 ");

        } else {
            tvAddApp.setImageResource(R.drawable.btnnoneapp); // App을 흑백 처리
            tvAddWeb.setImageResource(R.drawable.btnselectweb); // Web을 칼라 처리

            ivbtnAppApi.setVisibility(View.GONE);
            edtSetTime.setVisibility(View.GONE);

            edtLink.setHint("사이트 주소를 입력하세요");
            edtLink.setEnabled(true);

            // 값 세팅
            edtTitle.setText(titleData);
            edtText.setText(contentData);
            edtLink.setText(linkData);
            hour = (int) hourData;
            minute = (int) minuteData;
            indexes = indexesData;
            setTouchStatus = setTouchStatusData;

            edtSetTime.setEnabled(false);
            edtSetTime.setVisibility(View.VISIBLE);
            edtSetTime.setTextColor(Color.parseColor("#FFB1B1B1")); // 앱으로 선택된 링크는 수정이 불가능하다는 의미로 회색처리.
            edtSetTime.setText(hour + "시 " + minute + "분 " + indexes + "요일 ");
        }

        appAndWeb = isApp; // true는 app을 의미함.

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.i(TAG, "action: " + action);

                // 이 액션명을 받았을 때만 실행되도록
                if (action.equals("android.intent.action.save_app")) {
                    String message = ("액션 발생! " + intent.getAction());

                    checkManagerFragment.fragmentActivity.runOnUiThread(new Runnable() { // 메인 쓰레드를 사용하기 위해서
                        @Override
                        public void run() {
                            edtLink.setVisibility(View.VISIBLE);
                            SharedPreferences sharedPreferences = context.getSharedPreferences("REGISTER_DATA", Context.MODE_PRIVATE);
                            packagee = sharedPreferences.getString("uri", "no Register");
                            appName = sharedPreferences.getString("name", "no Register");
                            edtLink.setText(appName); // 이름을 설정해주는 것.
                            edtLink.setTextColor(Color.parseColor("#FFB1B1B1")); // 앱으로 선택된 링크는 수정이 불가능하다는 의미로 회색처리.
                            Global.dialogFlag = false;

                            Log.i(TAG, "가져온 패키지: " + packagee + "/가져온 이름: " + appName);
                        }
                    });
                } else if (action.equals("android.intent.action.gogo")) {
                    Toast.makeText(context, "다른거다~~", Toast.LENGTH_SHORT).show();

                } else if (action.equals("android.intent.action.save_time")) {
                    SharedPreferences preferences = context.getSharedPreferences("TIME_DATA", Context.MODE_PRIVATE);
                    edtSetTime.setEnabled(false);
                    edtSetTime.setVisibility(View.VISIBLE);
                    edtSetTime.setTextColor(Color.parseColor("#FFB1B1B1")); // 앱으로 선택된 링크는 수정이 불가능하다는 의미로 회색처리.
                    String hour = String.valueOf(preferences.getInt("hour", 0));
                    String minute = String.valueOf(preferences.getInt("minute", 0));
                    String indexes = preferences.getString("selected", "no");
                    edtSetTime.setText(hour + "시 " + minute + "분 " + indexes + "요일 ");
                    Log.i(TAG, "getintenttime");
                }
            }
        };

        // 동적으로 구현할때는 필터링 반드시 필요 (정적은 딱히 - 별도 클래스 구성)
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.save_app");
        filter.addAction("android.intent.action.gogo");
        filter.addAction("android.intent.action.save_time");
        context.registerReceiver(receiver, filter);

        // App
        tvAddApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAddApp.setImageResource(R.drawable.btnselectapp); // App을 칼라 처리
                tvAddWeb.setImageResource(R.drawable.btnnoneweb); // Web을 흑백 처리

                appAndWeb = true; // true는 app을 의미함.
                ivbtnAppApi.setVisibility(View.VISIBLE); // AppPackmanager 실행 버튼.
                edtSetTime.setVisibility(View.GONE);
                edtLink.setEnabled(false); // 입력을 하지 못하게 하기.
                edtLink.setHint("하단의 앱 등록하기를 터치하세요");
            }
        });

        // Web
        tvAddWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAddApp.setImageResource(R.drawable.btnnoneapp); // App을 흑백 처리
                tvAddWeb.setImageResource(R.drawable.btnselectweb); // Web을 칼라 처리
                ivbtnAppApi.setVisibility(View.GONE);
                edtSetTime.setVisibility(View.GONE);
                edtLink.setText("");
                edtLink.setHint("사이트 주소를 입력하세요");
                edtLink.setEnabled(true);

                appAndWeb = false; // false는 app을 의미함.
            }
        });

        ivbtnTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                AddTimePickerDialog addTimePickerDialog = new AddTimePickerDialog(context);
                addTimePickerDialog.callFunction();
            }
        });

        // 확인
        ivbtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (appAndWeb == true) { // true는 app을 의미함.
                        title = edtTitle.getText().toString();
                        content = edtText.getText().toString();
                        link = edtLink.getText().toString();

                        if (title.equals("") || content.equals("") || link.equals("")) {
                            Toast.makeText(context, "내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (packagee.equals("null")) {
                            Toast.makeText(context, "앱을 선택해주세요", Toast.LENGTH_SHORT).show();
                            return;
                        }


                    } else if (appAndWeb == false) { // false는 web을 의미함.
                        title = edtTitle.getText().toString();
                        content = edtText.getText().toString();
                        link = edtLink.getText().toString();

                        if (title.trim().equals("") || content.trim().equals("") || link.trim().equals("")) {
                            Toast.makeText(context, "내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    if (indexes.equals("no")) {
                        // 얘가 no라는 것은 시간의 데이터가 없으므로 데이터를 받아와야한다. 그즉슨 이건 말이 안되는 것이다.
                        // 0보다 크면 바로 업로드,
                    }

                    // HomeActivity에서 Login한 Id 가져오기.
//                getId = checkManagerFragment.id;
                    SharedPreferences sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
                    getId = sharedPreferences.getString("id", "no Register");

                    // 데이터 생성 후에는, 해당 데이터들이 없다. 직접 다시 설정하지 않으면 기본값은 ItemData의 값으로 지정한다.
                    sharedPreferences = context.getSharedPreferences("TIME_DATA", Context.MODE_PRIVATE);
                    hour = sharedPreferences.getInt("hour", (int) hourData); // 가져오지 않으면 기본값은 hourData
                    minute = sharedPreferences.getInt("minute", (int) minuteData); // 가져오지 않으면 기본값은 minuteData
                    indexes = sharedPreferences.getString("selected", indexesData); // 가져오지 않으면 기본값은 indexesData

                    // 출석시간 확인 할 타임의 defalut
                    Calendar calendar = Calendar.getInstance();
                    long setTime = calendar.getTimeInMillis();

                    Log.i(TAG, "AddDataDialog getId:" + getId);
                    Log.i(TAG, "title: " + title);
                    Log.i(TAG, "content: " + content);
                    Log.i(TAG, "link: " + link);
                    Log.i(TAG, "AppLink: " + appLink);
                    Log.i(TAG, "package: " + packagee);
                    Log.i(TAG, "hour: " + hour);
                    Log.i(TAG, "minute: " + minute);
                    Log.i(TAG, "indexes: " + indexes);

                    // 입력한 데이터 객체화
                    Map<String, Object> data = new HashMap<>();
                    data.put("title", title);
                    data.put("content", content);
                    data.put("link", link);
                    data.put("appLink", appLink);
                    data.put("hour", hour);
                    data.put("minute", minute);
                    data.put("indexes", indexes);
                    data.put("setTouchStatus", setTouchStatus);
                    data.put("checkTime", setTime);

                    if (appAndWeb) {
                        data.put("appAndWeb", "app");
                        data.put("package", packagee);

                    } else {
                        data.put("appAndWeb", "web");
                    }

                    // 데이터 업로드
                    db.collection(getId).document(title).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoidg) {
                            Log.i(TAG, "데이터를 성공적으로 입력하였습니다.");

                            Toast.makeText(context, "내용이 업로드되었습니다", Toast.LENGTH_SHORT).show();
                            dlg.dismiss();

                            Intent i = new Intent();
                            i.setAction("com.hscompnay.appchool.add_complete");
                            context.sendBroadcast(i);

                            // sharedFre TIME_DATA 초기화
                            SharedPreferences sharedPreferences = context.getSharedPreferences("TIME_DATA", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear().commit();

                            // 변수 초기화
                            hour = -1;
                            minute = -1;
                            indexes = "no";

                            // 하... 스파게티 코드다...진짜. 서로 다르면 전의 기록 삭제, 같으면 그대로.
                            if (!title.equals(titleData)) {
                                db.collection(getId).document(titleData).delete();
                            }
                        }
                    });

                    // SharedFreferences를 통하여 알람 시간대를 저장.
                    ItemData sharedFreferenceItemData = new ItemData(String.valueOf(appAndWeb), title, link, appLink, indexes, packagee, content, hour, minute, setTouchStatus);
                    SharedPreferences alarmFreferences = context.getSharedPreferences("ALARM", Context.MODE_PRIVATE);
                    SharedPreferences.Editor alarmEdit = alarmFreferences.edit();

                    //Gson API로 변환하여 SharedF에 넣을 객체 저장.
                    Gson gson = new Gson();

                    if (!title.equals(titleData)) {
                        alarmEdit.remove("DATA_" + titleData).commit();
                    }

                    String json = gson.toJson(sharedFreferenceItemData); // Json은 String으로 Return.
                    alarmEdit.putString("DATA_" + title, json); // 추가한 데이터를 집어넣는다.
                    alarmEdit.commit();

                    alarmData = alarmFreferences.getAll(); // SharedF에 ALARM.xml로 저장된 알람 정보들을 모두 가져온다.

                    // 데이터 갯수만큼 for(Map.Entry) 가져오고, 객체에 저장하기.
                    for (Map.Entry<String, ?> entry : alarmData.entrySet()) { // alarmData 내부의 데이터만큼 빼오기.
                        String alarmKey = entry.getKey(); // key
                        String a = (String) entry.getValue(); // value, Json은 String값으로 받는다.

                        // keySet을 통한 for문은 keySet이 String으로 Key값을 변환하는 것을 알음. 현재 AlarmData는 Title.
                        // KeySet은 길이만큼 불러오는 것이고, 불러오는 길이를 String 객체로 받아온다. :은 주로 객체형에 많이 쓰는 듯 하다. 자료형은 int 머시기로.

                        ItemData alarm = gson.fromJson(a, ItemData.class); // Json-ItemData로 변환.
                        String title = alarm.getTitle(); // 제목
                        String indexes = alarm.getIndexes(); // 요일
                        long hour = alarm.getHour(); // 시간
                        long minute = alarm.getMinute(); // 분
                        Log.i(TAG, "제목: " + title + "/요일: " + indexes + "/hour: " + hour + "/minute: " + minute);
                    }

                    // 알람등록
                    // AlaramManager로 시간 및 코드지정
                    regist(indexes, hour, minute, title, content);

                    // 데이터 모두 초기화


                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(context, "내용을 확인 할 수 없습니다. 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 설치된 앱 Api 불러오기
        ivbtnAppApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, getApplicationActivity.class);
                context.startActivity(intent);
            }
        });

        ivbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그인 한 ID 가져오기
                SharedPreferences sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
                getId = sharedPreferences.getString("id", "no Register");

                // 지울 ALARM 가져오기
                SharedPreferences sharedPreferences1 = context.getSharedPreferences("ALARM", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.remove("DATA_" + titleData).commit();

                db.collection(getId).document(itemData.getTitle()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, itemData.getTitle() + "삭제 완료!", Toast.LENGTH_SHORT).show();
                        dlg.dismiss();

                        Intent i = new Intent();
                        i.setAction("com.hscompnay.appchool.add_complete");
                        context.sendBroadcast(i);
                    }
                });
            }
        });
    }

    // app on
    // app off
    public void regist(String index, int hour, int minute, String title, String content) {

        Intent intent = new Intent("com.hscompany.event.alarm");

        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String[] indexes = index.split("/");

        Calendar calendar = Calendar.getInstance();

//         지정한 시간에 매일 알림
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
    }

    private int getWeekNumber(String week) {
        switch (week) {
            case "mon":
                return 1;
            case "thus":
                return 2;
            case "weds":
                return 3;
            case "thurs":
                return 4;
            case "fri":
                return 5;
            case "sat":
                return 6;
            case "sun":
                return 7;
            default:
                return -1;
        }
    }

    public void unregist() {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(pIntent);
    }

    public void setItem(ItemData itemData) {
//        this.itemData = new ItemData();
        this.itemData = itemData;
    }
}
