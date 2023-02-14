package com.hscompany.appchool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class getAppListAdapter extends BaseAdapter {

    Context context;

    ArrayList<GetItemData> datas = new ArrayList<>();

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.getapp_item, viewGroup, false);


        // 아이템 내에서 데이터 불러오기.
        ImageView setImg = view.findViewById(R.id.iv_getApp_item); // 사용할 이미지 뷰.
        TextView setText = view.findViewById(R.id.tv_getapp_item); // 사용할 텍스트 뷰.
        GetItemData getItemData = datas.get(i); // position에 위치한 아이템의 번호들?
        setImg.setImageDrawable(getItemData.itemImg); // 이미지 불러오기.
        setText.setText(getItemData.itemId); // 텍스트 불러오기

        return view;
    }

    public void getInfo(GetItemData getItemData) {
        datas.add(getItemData);
    }

    public ArrayList<GetItemData> getDatas() {
        return datas;
    }
}
