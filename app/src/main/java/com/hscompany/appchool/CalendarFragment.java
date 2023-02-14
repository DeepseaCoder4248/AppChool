package com.hscompany.appchool;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class CalendarFragment extends Fragment {

    Context context;
    View view;

    ImageView btnAfter;
    ImageView btnBefore;
    TextView tvYearMonth;
    RecyclerView rvCalendar;

    LocalDate selectedDate;

    AdView adView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (context == null) {
            context = getContext();
        }
        selectedDate = LocalDate.now(); // 현재 날짜 가져오기?, [,,,,1,2,3,4라는게 date에  plus,minusMonth를 하면 새로운 배열이 나타난다. 고로 날짜가 새로 바뀐다는 듯.

        view = inflater.inflate(R.layout.calendar_fragment, container, false);

        btnAfter = view.findViewById(R.id.btn_cal_frag_after);
        btnBefore = view.findViewById(R.id.btn_cal_frag_before);
        tvYearMonth = view.findViewById(R.id.tv_cal_frag_yearMonth);
        rvCalendar = view.findViewById(R.id.rv_cal_frag_calendar);

        adView = view.findViewById(R.id.adview_calfragment_admob);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        // 달력뷰 최초 설정

        setMonthView();

        btnAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusMonths(1); // 1월 추가.
                setMonthView();
            }
        });

        btnBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusMonths(1); // 1월 감소.
                setMonthView();
            }
        });

        return view;
    }


    private void setMonthView() {
        // 날짜를 TextView에 전시.
        String yearMonth = selectedDate.getYear() + "." + selectedDate.getMonth().getValue();
        Log.i("asdf", "what's getMonth().getValue?: " + selectedDate.getMonth().getValue());
        Log.i("asdf", "what's getMonth()?: " + selectedDate.getMonth());

        tvYearMonth.setText(yearMonth); // yearMonth를 전시.

        ArrayList<String> daysInMonth = currentDaysInMonth(selectedDate); // 해당 month를 리턴

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 7); // 리사이클러뷰의 카운트를 지정함. GridLayout으로(SKT 12인방 디자인)
        rvCalendar.setLayoutManager(layoutManager); // 레이아웃 매니저 설정.
        rvCalendar.setAdapter(calendarAdapter); // 어댑터 기능활성화
    }
    // 정리
    // LocalDate.getYear(): 현재연도 불러오기.
    // LocalDate.getMonth(): 현재 월 불러오기, 반환값은 January~December의 형태다.
    // LocalDate.getMonth().getValue(): 현재 월 불러오기, 반환값은 1~12.


    // 현재 달의 날짜를 리스트에 담아서 리턴함
    private ArrayList<String> currentDaysInMonth(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        Log.i("asdf", "daysInMonthArray: " + daysInMonthArray.size() + "");

        YearMonth yearMonth = YearMonth.from(date); // [selectedDate]date에서 선택되었던 연,월을 YearMonth로 반환.
        Log.i("asdf", "yearMonth: " + yearMonth + "");

        int daysInMonth = yearMonth.lengthOfMonth(); // 해당 월의 마지막 일을 가져옴. 10월이면 31일, 11월이면 30일.
        Log.i("asdf", "daysInMonth: " + daysInMonth + "");

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1); // [LocalDate]selectedDate에서 원하는 일자를 입력해서 반환.
        Log.i("asdf", "firstOfMonth: " + firstOfMonth + "");

        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); //
        Log.i("asdf", "dayOfWeek: " + dayOfWeek + "");


        for (int i = 1; i <= 42; i++) { // 42는 도대체 어디서 나온 숫자지? 내생각엔 RecyclerView가 뿌리는 갯수인가??
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                Log.i("asdf", "조건문 - i <= dayOfweek: " + "i: " + i + "<=" + dayOfWeek + " || "
                        + "i: " + i + ">" + "daysInMonth + dayOfWeek: " + daysInMonth + dayOfWeek);

                daysInMonthArray.add("");
                Log.i("asdf", "daysInMonthArray: " + daysInMonthArray + "");

                if (dayOfWeek == 3) {

                }

            } else {
                daysInMonthArray.add(yearMonth + "-" + String.valueOf(i - dayOfWeek));
                Log.i("asdf", "daysInMonthArray(String.valueOf(i-dayOfWeek)): " + daysInMonthArray + "");


            }
        }

        return daysInMonthArray;
    }

}
