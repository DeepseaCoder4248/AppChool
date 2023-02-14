package com.hscompany.appchool;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.Edits;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddTimePickerDialog implements View.OnClickListener {

    private Context context; // 다이어로그용

    // TimePicker
    TimePicker timePicker;
    LinearLayout laySetTime;
    int getHour = 0;
    int getMinute = 0;

//    // DayPicker
//    LinearLayout laySetDay;
//    ImageButton btnMon;
//    ImageButton btnTues;
//    ImageButton btnWed;
//    ImageButton btnThur;
//    ImageButton btnFri;
//    ImageButton btnSat;
//    ImageButton btnSun;

    LinearLayout laySetDay;
    LinearLayout laysetDaySub;
    ImageView btnMon;
    ImageView btnTues;
    ImageView btnWed;
    ImageView btnThur;
    ImageView btnFri;
    ImageView btnSat;
    ImageView btnSun;

    // Select
    int flag = 0; // Time,Date,Day 모드 설정
    ImageView btnNext; // 다음,확인 버튼

    // 요일을 값으로 받을 때 Boolean
    boolean isSelectedMonday = false;
    boolean isSelectedThuesday = false;
    boolean isSelectedWedsday = false;
    boolean isSelectedThursday = false;
    boolean isSelectedFriday = false;
    boolean isSelectedSatday = false;
    boolean isSelectedSunday = false;

    final static String TAG = AddTimePickerDialog.class.getSimpleName();

    ArrayList<Object> isDayList = new ArrayList<>(); // 요일값을 받았을 때 이곳에 저장.
    // 대충 ArrayList로는 Boolean만 받으므로 Key값을 넣어주고 Value값을 받아오는 방식인 HashMap을 사용할 것으로 생각됨.

    //    Map<String, Boolean> isDayList2 = new HashMap<>();
    // 월,화,수,목,금,토,일
    // 0: 월
    // 1: 화
    // 2: 수
//    ArrayList<Boolean> isDayList2 = new ArrayList<>();
    boolean[] isDayList2 = {false, false, false, false, false, false, false};


    // 다이어로그 메소드
    public AddTimePickerDialog(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void callFunction() {
        Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_add_data_timepick); // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.show(); // 커스텀 다이얼로그를 노출한다.

        // xml 연동
        timePicker = dlg.findViewById(R.id.picker_timepicker);
        laySetTime = dlg.findViewById(R.id.lay_timepick_timepicker);
        laySetDay = dlg.findViewById(R.id.selectWeekMain);
        laysetDaySub = dlg.findViewById(R.id.selectWeekSub);
        btnMon = dlg.findViewById(R.id.btn_timepick_monday);
        btnTues = dlg.findViewById(R.id.btn_timepick_tuesday);
        btnWed = dlg.findViewById(R.id.btn_timepick_wendesday);
        btnThur = dlg.findViewById(R.id.btn_timepick_thursday);
        btnFri = dlg.findViewById(R.id.btn_timepick_friday);
        btnSat = dlg.findViewById(R.id.btn_timepick_sataurday);
        btnSun = dlg.findViewById(R.id.btn_timepick_sunday);
        btnNext = dlg.findViewById(R.id.ibtn_timepick_next);

        // 익명클래스
        // 인터페이스
        // 업캐스팅
        btnNext.setOnClickListener(this);
        btnMon.setOnClickListener(this);
        btnTues.setOnClickListener(this);
        btnWed.setOnClickListener(this);
        btnThur.setOnClickListener(this);
        btnFri.setOnClickListener(this);
        btnSat.setOnClickListener(this);
        btnSun.setOnClickListener(this);

        // timePicker 선택 시 변경하기 전까지는 현재 시간대를 가져온다.
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        timePicker.setHour((int) date.getHours());
        timePicker.setMinute(date.getMinutes());
        Log.i("TIMES", date + "");


        // 선택시 time 출력하는 리스너.
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                getHour = i;
                getMinute = i1;

                Log.i("ADD_TIME", "HOUR:" + getHour + ", MINUTE:" + getMinute);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 다음 버튼을 눌렀을때에는 Date가 나오므로, 시간을 선택하기 위해서 다음을 누르지 않는다. 그래서 추가하지 않는다.

                // Date 화면으로 변경.
                if (flag == 0) {
                    flag = 1;
                    laySetTime.setVisibility(View.GONE);
                    laySetDay.setVisibility(View.VISIBLE);
                    laysetDaySub.setVisibility(View.VISIBLE);

                    Log.i(TAG, "flag: " + flag);
                    Log.i(TAG, "HOUR:" + getHour + ", MINUTE:" + getMinute);


                    // 반복 설정 화면으로 변경.
                } else if (flag == 1) {
                    // Set, Iterator 웬만하면 쓰지말기
//

                    // True인 요일이 몇개인지 예상은 안됨
                    ArrayList<String> selectedIndex = new ArrayList<>();

                    for (int i = 0; i < isDayList2.length; i++) {
                        if (isDayList2[i] == true) {

                            switch (i) {
                                case 0:
                                    selectedIndex.add("월");
                                    break;
                                case 1:
                                    selectedIndex.add("화");
                                    break;
                                case 2:
                                    selectedIndex.add("수");
                                    break;
                                case 3:
                                    selectedIndex.add("목");
                                    break;
                                case 4:
                                    selectedIndex.add("금");
                                    break;
                                case 5:
                                    selectedIndex.add("토");
                                    break;
                                case 6:
                                    selectedIndex.add("일");
                                    break;
                                default:
                                    break;
                            }
                            Log.i("TAG", "selected day: " + i);

                        }
                    }

                    Log.i("TAG", "Real Index: " + selectedIndex);

                    Log.i("TAG", "마무리~");

                    Log.i("TAG", "length: " + isDayList2.length);

                    // 요일 선택안됐다
                    // 2) isDayList2안의 요소가 모두 false (length7)


                    // false, false, false, false, false, false, false
                    // true, false, true, false, true, true, false
                    // false, false, true, false, true, true, false

                    // 방법 1
//                    int checkIndex = 0;
//                    for (int i = 0; i < isDayList2.length; i++) {
//
//                        Log.i("TAG", "v: " + isDayList2[i]);
//                        if (isDayList2[i] == false) {
//                            checkIndex++;
//                        }
//
//                    }
//                    Log.i("TAG", "value: " + checkIndex);

                    // false, false, false, false, false, false, false
                    // true, false, true, false, true, true, false
                    // false, false, true, false, true, true, false

                    // 방법 2
                    boolean isBasic = false;
                    for (int i = 0; i < isDayList2.length; i++) {
                        if (isDayList2[i] == true) isBasic = true;
                    }

                    // 방법 3
                    // 자바8이상 지원: 람다식
                    // 리스트객체.filter (조건 비교)

                    // 방법 4
                    // 리스트객체.reduce (값 비교)


                    if (isBasic == false) {
                        Log.i("TAG", "오류~~");
                        Toast.makeText(context, "요일을 선택하여 주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences preferences = context.getSharedPreferences("TIME_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putInt("hour", getHour);
                        editor.putInt("minute", getMinute);

                        String indexes = "";
                        for (int i = 0; i < selectedIndex.size(); i++) {
                            indexes = indexes + "/" + selectedIndex.get(i);
                        }
                        Log.i("TAG", "Selected Indexes" + indexes);
                        editor.putString("selected", indexes.substring(1));

                        editor.commit();

                        // 현재 다이얼로그 종료

                        // 1) SharedPreference
                        // 2) 리턴개념

                        // 선택된 요일
                        // 년, 월, 일, 시, 분
                        // 제목
                        // 내용
                        // 앱 이름
                        // 앱/웹

                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.save_time");
                        context.sendBroadcast(intent);
                        Log.i("ADD_TIME", intent + "");
                        dlg.dismiss();
                    }
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_timepick_next:
                // 다음 버튼을 눌렀을때에는 Date가 나오므로, 시간을 선택하기 위해서 다음을 누르지 않는다. 그래서 추가하지 않는다.

                // Date 화면으로 변경.
                if (flag == 0) {
                    flag = 1;
                    laySetTime.setVisibility(View.GONE);

                    Log.i("TAG", "flag: " + flag);

                    // 반복 설정 화면으로 변경.
                } else if (flag == 1) {
                    flag = 0;
                    laySetDay.setVisibility(View.VISIBLE);
                    laysetDaySub.setVisibility(View.VISIBLE);

                    Log.i("TAG", "flag: " + flag);
                }
                break;
            case R.id.btn_timepick_monday:
                if (isSelectedMonday) {
                    isSelectedMonday = false;
                    isDayList2[0] = false;
                    btnMon.setImageResource(R.drawable.checkmanager_adddate_day__none_mon);
                } else {
                    isSelectedMonday = true;
                    isDayList2[0] = true;
                    btnMon.setImageResource(R.drawable.checkmanager_adddate_day__select_mon);
                }
                isDayList.add(isSelectedMonday); // 월요일 True,False를 isDayList 배열에 저장.
//                isDayList2.put("monday", isSelectedMonday); // 위와 같지만 Key값으로 구분함.

                // 어레이리스트에 값 추가하는 방법

                // 리스트 (인덱스 -> 값)
                // 맵 (키 -> 값)


                Log.i("TAG", "MONDAY");
                Log.i("TAG", "현재상태: " + isSelectedMonday);
                break;

            case R.id.btn_timepick_tuesday:
                if (isSelectedThuesday) {
                    isSelectedThuesday = false;
                    isDayList2[1] = false;
                    btnTues.setImageResource(R.drawable.checkmanager_adddate_day__none_tues);
                } else {
                    isSelectedThuesday = true;
                    isDayList2[1] = true;
                    btnTues.setImageResource(R.drawable.checkmanager_adddate_day__select_thes);
                }
                isDayList.add(isSelectedThuesday);// 화요일 True,False를 isDayList 배열에 저장.
//                isDayList2.put("tuesday", isSelectedThuesday); // 위와 같지만 Key값으로 구분함.


                Log.i("TAG", "TUES");
                Log.i("TAG", "현재상태: " + isSelectedThuesday);
                break;

            case R.id.btn_timepick_wendesday:
                if (isSelectedWedsday) {
                    isSelectedWedsday = false;
                    isDayList2[2] = false;
                    btnWed.setImageResource(R.drawable.checkmanager_adddate_day__none_wed);
                } else {
                    isSelectedWedsday = true;
                    isDayList2[2] = true;
                    btnWed.setImageResource(R.drawable.checkmanager_adddate_day__select_wed);
                }
                isDayList.add(isSelectedWedsday); // 수요일 True,False를 isDayList 배열에 저장.
//                isDayList2.put("wendsday", isSelectedWedsday); // 위와 같지만 Key값으로 구분함.


                Log.i("TAG", "WEDS");
                Log.i("TAG", "현재상태: " + isSelectedWedsday);
                break;

            case R.id.btn_timepick_thursday:
                if (isSelectedThursday) {
                    isSelectedThursday = false;
                    isDayList2[3] = false;
                    btnThur.setImageResource(R.drawable.checkmanager_adddate_day__none_thur);
                } else {
                    isSelectedThursday = true;
                    isDayList2[3] = true;
                    btnThur.setImageResource(R.drawable.checkmanager_adddate_day__select_thur);
                }
                isDayList.add(isSelectedThursday); // 목요일 True,False를 isDayList 배열에 저장.
//                isDayList2.put("thursday", isSelectedThursday); // 위와 같지만 Key값으로 구분함.


                Log.i("TAG", "THURS");
                Log.i("TAG", "현재상태: " + isSelectedThursday);
                break;

            case R.id.btn_timepick_friday:
                if (isSelectedFriday) {
                    isSelectedFriday = false;
                    isDayList2[4] = false;
                    btnFri.setImageResource(R.drawable.checkmanager_adddate_day__none_fri);
                } else {
                    isSelectedFriday = true;
                    isDayList2[4] = true;
                    btnFri.setImageResource(R.drawable.checkmanager_adddate_day__select_fri);
                }
                isDayList.add(isSelectedFriday); // 금요일 True,False를 isDayList 배열에 저장.
//                isDayList2.put("friday", isSelectedFriday); // 위와 같지만 Key값으로 구분함.


                Log.i("TAG", "FRI");
                Log.i("TAG", "현재상태: " + isSelectedFriday);
                break;

            case R.id.btn_timepick_sataurday:
                if (isSelectedSatday) {
                    isSelectedSatday = false;
                    isDayList2[5] = false;
                    btnSat.setImageResource(R.drawable.checkmanager_adddate_day__none_sat);
                } else {
                    isSelectedSatday = true;
                    isDayList2[5] = true;
                    btnSat.setImageResource(R.drawable.checkmanager_adddate_day__select_sat);
                }
                isDayList.add(isSelectedSatday); // 토요일 True,False를 isDayList 배열에 저장.
//                isDayList2.put("satuarday", isSelectedSatday); // 위와 같지만 Key값으로 구분함.


                Log.i("TAG", "SAT");
                Log.i("TAG", "현재상태: " + isSelectedSatday);
                break;

            case R.id.btn_timepick_sunday:
                if (isSelectedSunday) {
                    isSelectedSunday = false;
                    isDayList2[6] = false;
                    btnSun.setImageResource(R.drawable.checkmanager_adddate_day__none_sun);
                } else {
                    isSelectedSunday = true;
                    isDayList2[6] = true;
                    btnSun.setImageResource(R.drawable.checkmanager_adddate_day__select_sun);
                }
                isDayList.add(isSelectedSunday); // 일요일 True,False를 isDayList 배열에 저장.
//                isDayList2.put("sunday", isSelectedSunday); // 위와 같지만 Key값으로 구분함.


                Log.i("TAG", "SUN");
                Log.i("TAG", "현재상태: " + isSelectedSunday);
                break;
        }
    }
}

