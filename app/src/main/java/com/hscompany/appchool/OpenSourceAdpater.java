package com.hscompany.appchool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class OpenSourceAdpater extends BaseAdapter {

    ArrayList<OpenSourceDO> datas = new ArrayList<>();
    Context context;

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
        if (view == null) {
            context = viewGroup.getContext();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.listview_opensource, viewGroup, false);
        }

        TextView tvOpenSourceTitle = view.findViewById(R.id.tvOpenSourceTitle);
        TextView tvOpenSourceDesc = view.findViewById(R.id.tvOpenSourceDesc);
        TextView tvOpenSourceETC = view.findViewById(R.id.tvOpenSourceETC);

        OpenSourceDO data = datas.get(i);

        String title = data.getName();
        String description = data.getDescription();
        String etc = data.getEtc();

        tvOpenSourceTitle.setText(title);
        tvOpenSourceDesc.setText(description);
        tvOpenSourceETC.setText(etc);


        return view;
    }

    public void addItem(OpenSourceDO data) {
        datas.add(data);
    }
}
