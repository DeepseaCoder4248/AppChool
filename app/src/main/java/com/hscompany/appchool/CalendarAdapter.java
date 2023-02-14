package com.hscompany.appchool;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

// 내부클래스(inner class)
public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    ArrayList<String> daysOfMonth;
    Context context;


    public CalendarAdapter(ArrayList<String> daysOfMonth) {
        this.daysOfMonth = daysOfMonth; // ArrayList<String>날짜 값 받아옴.
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_calendar_cell,parent,false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int)(parent.getHeight() * 0.166666666);

        // holder만들어서 리턴하는것은 한번밖에 사용안하니까 낭비임
        // 즉 new하고 바로 리턴하는게 효율적임
//        CalendarViewHolder holder = new CalendarViewHolder(view);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        // arrayList에 담아둔 값을 가져와서 실제로 뿌리는 부분
        Log.i("gugu", "v: " + daysOfMonth.get(position));
        String getDay = daysOfMonth.get(position);
        String date = "";
        if(getDay != "") {
            date = daysOfMonth.get(position).split("-")[2];
        }


        holder.tvDayOfMonth.setText(date);

        // sharedpreference에서 100%로 저장된 날짜를 불러옴
        // 1) 현재날짜와 daysOfMonth.get(position)값이 같다면
        // 2) 이미지  visible
        // 3) 기본은 invisible

//        imgCheckComplete
        SharedPreferences preferences = context.getSharedPreferences("CALENDAR", Context.MODE_PRIVATE);

        // 키 값 다 가져오기
        Map<String, ?> keys = preferences.getAll();
        ArrayList<String> realKeys = new ArrayList<>();

        // SharedPreference의 키값들 수집
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            realKeys.add(entry.getKey());
        }

        for(int i=0; i<realKeys.size(); i++) {
            String perpectDate = realKeys.get(i);
            if(perpectDate.equals(getDay)) {
                holder.imgCheckComplete.setVisibility(View.VISIBLE);
            }
        }

        holder.imgCheckComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 이미지를 클릭했을때

                Toast.makeText(context, getDay + "눌렸다~", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }
}
