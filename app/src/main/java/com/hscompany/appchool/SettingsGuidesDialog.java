package com.hscompany.appchool;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class SettingsGuidesDialog {

    Dialog dlg;

    ViewPager2 viewPager2;

    SettingsGuidesDialog(Context context) {

        dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.settings_guides_dialog);

        viewPager2 = dlg.findViewById(R.id.vp_settings_guides_content);

        // 이미지 집어넣기
        ArrayList<SettingsGuidesItemData> list = new ArrayList<>();

        Drawable img1 = dlg.getContext().getDrawable(R.drawable.settings_guides1);
        list.add(new SettingsGuidesItemData(img1));
        Drawable img2 = dlg.getContext().getDrawable(R.drawable.settings_guides2);
        list.add(new SettingsGuidesItemData(img2));
        Drawable img3 = dlg.getContext().getDrawable(R.drawable.settings_guides3);
        list.add(new SettingsGuidesItemData(img3));
        Drawable img4 = dlg.getContext().getDrawable(R.drawable.settings_guides4);
        list.add(new SettingsGuidesItemData(img4));
        Drawable img5 = dlg.getContext().getDrawable(R.drawable.settings_guides5);
        list.add(new SettingsGuidesItemData(img5));
        Drawable img6 = dlg.getContext().getDrawable(R.drawable.settings_guides6);
        list.add(new SettingsGuidesItemData(img6));
        Drawable img7 = dlg.getContext().getDrawable(R.drawable.settings_guides7);
        list.add(new SettingsGuidesItemData(img7));

        viewPager2.setAdapter(new SettingsGuidesViewPagerAdapter(list));

//        // 이미지 가져와지는지 테스트 여부.
//        ImageView ivTest = dlg.findViewById(R.id.iv_test_settings);
//        Drawable drawable = dlg.getContext().getResources().getDrawable(R.drawable.introduce);
//        ivTest.setImageDrawable(drawable);

        dlg.show();
    }
}
