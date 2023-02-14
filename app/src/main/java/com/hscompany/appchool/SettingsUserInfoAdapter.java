package com.hscompany.appchool;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;


public class SettingsUserInfoAdapter extends BaseAdapter {

    Context context;
    ArrayList<SettingsUserInfoItem> datas = new ArrayList<>();

    String TAG = SettingsUserInfoAdapter.class.getSimpleName();

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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.settings_user_info_item, viewGroup, false);

        TextView tvTitle = view.findViewById(R.id.tv_settings_user_info_title);
        TextView tvSubTitle = view.findViewById(R.id.tv_settings_user_info_subtitle);

        SettingsUserInfoItem getItem = datas.get(i);

        if (tvSubTitle.equals("")) {
            tvTitle.setText(getItem.title);
            tvSubTitle.setVisibility(View.GONE);

        } else {
            tvTitle.setText(getItem.title);
            tvSubTitle.setText(getItem.subTitle);
            tvSubTitle.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void addItem(SettingsUserInfoItem itemData) {
        datas.add(itemData);
        Log.i(TAG, datas.size() + "");
    }
}
