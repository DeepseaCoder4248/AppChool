package com.hscompany.appchool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class IntroViewPager extends PagerAdapter {

    private Context mContext;
    int i = 0;

    public IntroViewPager() {
    }

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public IntroViewPager(Context context) {
        mContext = context;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = null;

        if (mContext != null) {
            // LayoutInflater를 통해 "/res/layout/page.xml"을 뷰로 생성.
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.page, container, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.ivIntro);
            ImageView imgbtn = (ImageView) view.findViewById(R.id.btn_introduce_ok);

            // 현재 페이지넘버 확인하여 소개 이미지 넣어주기
            switch (position) {
                case 0:
                    imageView.setImageResource(R.drawable.troduceex_appchool);
                    break;

                case 1:
                    imageView.setImageResource(R.drawable.troduceex_setting);
                    break;

                case 2:
                    imageView.setImageResource(R.drawable.troduceex_start);

                    if (i == 0) {
                        i = 1;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imgbtn.setVisibility(View.VISIBLE);
                            }
                        }, 2000);

                    } else if (i == 1) {
                        imgbtn.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            imgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);

                    // 객체단위의 캐스팅
                    // 업캐스팅, 다운캐스팅 (상속관계에 사용가능)
                    // 업캐스팅 (자식 -> 부모)
                    // 다운캐스팅 (부모 -> 자식)

                    // 자식이 내용이 크고,  부모가 더 작다
                    // Context부모
                    // Activity자식
                    // Context -> Activity

                    
                    // int: 부모
                    // double: 자식
                    int b = 3;
                    double a = b;

                    // acitvity: 자식
                    // context: 부모
//                    Activity activity = (Activity)mContext;
//                    activity.finish();
                    ((Activity)mContext).finish();
                }
            });
        }

        // 뷰페이저에 추가.
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }
}

