package com.hscompany.appchool;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SettingsGuidesViewHolderPage extends RecyclerView.ViewHolder {

    private LinearLayout layAdapter;
    private ImageView ivSetImage;

    SettingsGuidesItemData data;


    public SettingsGuidesViewHolderPage(@NonNull View itemView) {
        super(itemView);

        layAdapter = itemView.findViewById(R.id.lay_settings_guides_viewPagerAdapter_item);
        ivSetImage = itemView.findViewById(R.id.iv_settings_guides_adapter_setImage);

    }


    public void onBind(SettingsGuidesItemData data) {
        this.data = data;

        ivSetImage.setImageDrawable(data.getImage());

    }
}
