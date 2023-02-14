package com.hscompany.appchool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class HomeActivity extends AppCompatActivity {

//    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    MainMenuFragment mainMenuFragment;
    CheckManagerFragment checkManagerFragment;
    CalendarFragment calendarFragment;
    SettingsFragment settingsFragment;
    FrameLayout fragmentContainer;
    SharedPreferences sharedPreferences;

    ImageView btnMainMenu;
    ImageView btnCheckManager;
    ImageView btnCalendar;
    ImageView btnSettings;

    String Id;

    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentContainer = findViewById(R.id.fragmentContainer);
        btnCalendar = findViewById(R.id.btn_home_calendar);
        btnCheckManager = findViewById(R.id.btn_home_check_manager);
        btnMainMenu = findViewById(R.id.btn_home_mainmenu);
        btnSettings = findViewById(R.id.btn_home_settings);

        //최초 화면
        mainMenuFragment = new MainMenuFragment();
        Global.fragmentManager = getSupportFragmentManager();
        fragmentTransaction = Global.fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, mainMenuFragment);
        fragmentTransaction.commit();
        // 하나의 트랜잭션은 하나의 commit

        // 로그인하면 ID 가져오기
        Intent intent = getIntent();
        Id = intent.getStringExtra("id");
        Log.i("TAG", "로그인 된 ID:" + Id);

        // 쉐어드 프리퍼런스의 이름은 포괄적으로 짓기
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", Id); // User / id / Id 객체
        editor.commit();

        // 메인 메뉴
        btnMainMenu.setImageDrawable(getDrawable(R.drawable.main_btn_mainmenu_selected));
        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMainMenu.setImageDrawable(getDrawable(R.drawable.main_btn_mainmenu_selected)); // 메인메뉴 선택된 버튼
                btnCheckManager.setImageResource(R.drawable.main_btn_cm); // 출석관리 해제된 버튼
                btnCalendar.setImageDrawable(getDrawable(R.drawable.main_btn_checkcalander)); // 출석달력 해제된 버튼
                btnSettings.setImageResource(R.drawable.main_btn_options); // 환경설정 해제된 버튼

                mainMenuFragment = new MainMenuFragment();
                fragmentTransaction = Global.fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, mainMenuFragment).commit();
            }
        });

        // 출석 관리
        btnCheckManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCheckManager.setImageDrawable(getDrawable(R.drawable.main_btn_checkmanager_selected)); // 출석관리 선택된 버튼
                btnMainMenu.setImageResource(R.drawable.main_btn_mainmenu); // 메인메뉴 해제된 버튼
                btnCalendar.setImageDrawable(getDrawable(R.drawable.main_btn_checkcalander)); // 출석달력 해제된 버튼
                btnSettings.setImageResource(R.drawable.main_btn_options); // 환경설정 해제된 버튼

                checkManagerFragment = new CheckManagerFragment();
                fragmentTransaction = Global.fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, checkManagerFragment).commit();
            }
        });

        // 출석 달력
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCalendar.setImageDrawable(getDrawable(R.drawable.main_btn_checkcalender_selected)); // 출석달력 선택된 버튼
                btnMainMenu.setImageResource(R.drawable.main_btn_mainmenu); // 메인메뉴 해제된 버튼
                btnCheckManager.setImageDrawable(getDrawable(R.drawable.main_btn_cm)); // 출석관리 해제된 버튼
                btnSettings.setImageResource(R.drawable.main_btn_options); // 환경설정 해제된 버튼

                calendarFragment = new CalendarFragment();
                fragmentTransaction = Global.fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, calendarFragment).commit();
            }
        });

        // 환경 설정
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSettings.setImageDrawable(getDrawable(R.drawable.main_btn_options_selected)); // 환경설정 선택된 버튼
                btnMainMenu.setImageDrawable(getDrawable(R.drawable.main_btn_mainmenu)); // 메인메뉴 해제된 버튼
                btnCheckManager.setImageDrawable(getDrawable(R.drawable.main_btn_cm)); // 출석관리 해제된 버튼
                btnCalendar.setImageDrawable(getDrawable(R.drawable.main_btn_checkcalander)); // 출석달력 해제된 버튼

                settingsFragment = new SettingsFragment();
                fragmentTransaction = Global.fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, settingsFragment).commit();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.i("ZXCV", "앱 종료");
        super.onDestroy();
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }

    public void showGuide() {
        Toast.makeText(getApplicationContext(), "한번 더 터치시 종료됩니다.", Toast.LENGTH_SHORT).show();
    }

}
