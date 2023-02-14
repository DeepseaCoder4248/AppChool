package com.hscompany.appchool;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    Context context;
    View view;

    TextView tvSettingsId;
    TextView tvOpenSource;
    FrameLayout flGuides;
    FrameLayout flAS;
    FrameLayout flDeleteUserInfo;
    FrameLayout flUserInfo;
    Switch swAutoLoginSelect;

    SettingsGuidesDialog settingsGuidesDialog;
    SettingsDeleteUserDialog settingsDeleteUserDialog;

    SharedPreferences sharedPreferences;

    String TAG2 = "SF";
    String TAG = SettingsFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 리소스 사용 중복을 방지하기 위해 Context 한번 설정.
        if (context == null) {
            context = getContext();
        }
        view = inflater.inflate(R.layout.settings_fragment, container, false);

        // 기능
        tvSettingsId = view.findViewById(R.id.tv_settings_id); // id 전시
        tvOpenSource = view.findViewById(R.id.tvOpenSource); // 오픈소스 라이센스
        flUserInfo = view.findViewById(R.id.fl_settings_set_user_info); // 비밀번호 변경
        flGuides = view.findViewById(R.id.fl_settings_guides); // 사용법
        flAS = view.findViewById(R.id.fl_settings_AS); // 정보 및 터치시 메일 보내기 기능
        swAutoLoginSelect = view.findViewById(R.id.sw_settings_autoLoginSelect); // 자동 로그인 설정 스위치


        // login한 id 가져와서 id 나오는 곳에 넣기.
        sharedPreferences = context.getSharedPreferences("USER", context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", "null");

        // 자동 로그인 설정 값 불러오기.
        String autoLoginStatus = sharedPreferences.getString("autoLoginStatus", "0");
        Log.i(TAG2 + "_LOGIN", autoLoginStatus);

        // 자동 로그인 설정 값이 1이면 버튼이 checked되어 전시.
        if (autoLoginStatus.equals("1")) {
            swAutoLoginSelect.setChecked(true);
        }

        // 로그인되면 전시하기.
        if (!id.equals("null")) {
            tvSettingsId.setText(id + "님");
        }

        flUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingsUserInfo.class);
                startActivity(intent);
            }
        });

        tvOpenSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, OpenSourceActivity.class);
                startActivity(i);
            }
        });

        // 자동 로그인 설정 스위치 버튼
        swAutoLoginSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                SharedPreferences.Editor edit = sharedPreferences.edit(); // shardF을 사용하여 다음 로그인 시 해당 값을 저장.

                if (b == true) { // 1은 자동 로그인 설정.
                    edit.putString("autoLoginStatus", "1"); // 1은 활성화
                    edit.putString("autoLoginID", id); // 자동로그인 할 id 추가
                    edit.commit();

                } else { // 2는 자동 로그인 해제.
                    edit.putString("autoLoginStatus", "0"); // 0은 비활성화
                    edit.remove("autoLoginID"); // 자동 로그인 할 id 삭제
                    edit.commit();
                }
            }
        });

        // 사용법 Dialog
        flGuides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsGuidesDialog = new SettingsGuidesDialog(context);
            }
        });

//        flDeleteUserInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                settingsDeleteUserDialog = new SettingsDeleteUserDialog(context);
//
//            }
//        });


        return view;
    }
}
