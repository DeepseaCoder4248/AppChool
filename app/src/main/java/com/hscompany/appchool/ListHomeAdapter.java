package com.hscompany.appchool;

import android.content.Context;
import android.graphics.Paint;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ListHomeAdapter extends BaseAdapter {

    ArrayList<ItemData> datas = new ArrayList<>();
    Context context;
    int fragmentSwitch;

    //string,int,string

    @Override
    public int getCount() { // 아이템 갯수
        return datas.size();
    }

    @Override
    public Object getItem(int i) { //
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) { // 아이템 순서


        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 안드로이드 시스템에서 인플레이팅 객체 얻어옴
        if (view == null) {
            context = viewGroup.getContext();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.home_item, viewGroup, false);
        }

        TextView itemTitle = view.findViewById(R.id.tv_home_item_title);
        TextView itemTime = view.findViewById(R.id.tv_item_time);
        TextView itemContent = view.findViewById(R.id.tv_home_item_content);
//        rbtnSelect = view.findViewById(R.id.radioBtn_home_item_select);

        ItemData data = datas.get(i);
        String title = data.getTitle();
        String content = data.getContent();
        String hour = String.valueOf(data.getHour());
        String minute = String.valueOf(data.getMinute());
        String indexes = data.getIndexes();
        boolean touchStatus = data.getSetTouchStatus();
        String[] weeks = indexes.split("/");
        String result = "";

        // 반복문의 break
        // switch-case break
        for (int pos = 0; pos < weeks.length; pos++) {
            Log.i("gugu", weeks[pos]);

//            String temp = getWeek(weeks[pos]);
//            result = result + temp;


            result = result + getWeek(weeks[pos]);

        }

        // ,이전까지 인덱스(목. 이런식으로 나오면 -1을 통해서 목 으로만 전시하게 하기)
        result = result.substring(0, result.length() - 1);

        // textview에 전시하기.
        itemTime.setText(result + " " + hour + "시 " + minute + "분");
        itemTitle.setText(title);
        itemContent.setText(content);

        if (fragmentSwitch == 0) {

            // 다음에 불러올 때, 터치가 된 항목은 취소줄을 실행시킴.
            if (touchStatus == false) {
                itemTitle.setPaintFlags(itemTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                itemContent.setPaintFlags(itemContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                itemTime.setPaintFlags(itemTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            출처: https://bablabs.tistory.com/55 [BAB LABS]
            }
        }
        return view;
    }

    public String getWeek(String week) {
        // 단순히 값 매칭
        // 값이 맞냐 안맞냐 조건밖에 모름
        switch (week) {
            case "월":
                week = "월,";
                break;
            case "화":
                week = "화,";
                break;
            case "수":
                week = "수,";
                break;
            case "목":
                week = "목,";
                break;
            case "금":
                week = "금,";
                break;
            case "토":
                week = "토,";
                break;
            case "일":
                week = "일,";
                break;
            default:
                break;
        }
        return week;
    }

    public void addItem(ItemData data) {
        datas.add(data);
    }

    public ItemData setItem(int i) { // 외부에서 선택한 거를 입력받고 그에 맞는 걸 아 이런걸 어떻게 따로 정리해놓지? 나만 이해할 수 있게.(교육용 아님) 첫 position(i)값은 MainMenuFragment에서 받기 때문이다.
        ItemData itemData = datas.get(i);
        return itemData;
    }

    public void fragmentUseCancelLine(int swith) {
        fragmentSwitch = swith;
    }
}
