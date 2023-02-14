package com.hscompany.appchool;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CalendarViewHolder extends RecyclerView.ViewHolder {

    TextView tvDayOfMonth;
    ImageView imgCheckComplete;
    public CalendarViewHolder(@NonNull View itemView) {
        super(itemView);

        tvDayOfMonth = itemView.findViewById(R.id.tv_item_calender_cellDay);
        imgCheckComplete = itemView.findViewById(R.id.img_item_calender_check);
        
        // 인터페이스 응용코드
    }
}
