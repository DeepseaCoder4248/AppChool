package com.hscompany.appchool;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingsGuidesViewPagerAdapter extends RecyclerView.Adapter<SettingsGuidesViewHolderPage> {

    ArrayList<SettingsGuidesItemData> data;

    public SettingsGuidesViewPagerAdapter(ArrayList<SettingsGuidesItemData> data) {
        this.data = data;
    }

    @Override
    public SettingsGuidesViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.settings_guides_view_pager_adapter_item, parent, false);
        return new SettingsGuidesViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsGuidesViewHolderPage holder, int position) {
        if (holder instanceof SettingsGuidesViewHolderPage) {
            SettingsGuidesViewHolderPage viewHolder = holder;
            viewHolder.onBind(data.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
