package com.hscompany.appchool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

public class IntroduceActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private IntroViewPager pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new IntroViewPager(this);
        viewPager.setAdapter(pagerAdapter);
        // Activity > Context
    }
}