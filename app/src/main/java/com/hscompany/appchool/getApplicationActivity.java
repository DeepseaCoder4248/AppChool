package com.hscompany.appchool;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class getApplicationActivity extends AppCompatActivity {

    getAppListAdapter getAppListAdapter;
    ListView lvgetApp;
    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_application);

        getAppListAdapter = new getAppListAdapter();
        PackageManager packageManager = getPackageManager(); // 패키지 매니저
        List<ApplicationInfo> packgelist = packageManager.getInstalledApplications(0); // 설치된 앱 가져오기?
        for (int i = 0; i < packgelist.size(); i++) { // 이 기능을 패키지 리스트 사이즈만큼 반복하기
            ApplicationInfo packinfo = packgelist.get(i);
            Log.i("TAG", packinfo + "");

            String test = (String) packinfo.loadLabel(packageManager);
            String test2 = packinfo.packageName;
            Log.i("loadInfo", "레이블:" + test + "/com명의 이름:" + test2);
            // 앱명이 나타나면 잘 불러와진다. 시발 어디에도 이런 로드하는 정보를 안 알려주네. 메소드로 겨우 찿음.

            // 아이템의 데이터를 얻기.
            lvgetApp = findViewById(R.id.lv_getApplication); // 사용할 ListView 지정해서 어댑터 준비하기.
            GetItemData getItemData = new GetItemData(); // ItemData 객체.
            getItemData.itemId = (String) packinfo.loadLabel(packageManager); // 텍스트를 로딩하는 듯.
            getItemData.itemImg = packinfo.loadIcon(packageManager); // 이미지를 로딩하는 듯.
            getItemData.itemApiUri = packinfo.packageName;
            getAppListAdapter.getInfo(getItemData); // 아이템을 넣어서 적용시켜 줄 어댑터.
            lvgetApp.setAdapter(getAppListAdapter); // 어댑터를 넣어서 최종적으로 구동시키기.
            // https://imleaf.tistory.com/70
            // https://codechacha.com/ko/android-query-installed-packages/
            // https://soulduse.tistory.com/85
        }

        lvgetApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // test 1
                ArrayList<GetItemData> getDatas = getAppListAdapter.getDatas();
                GetItemData data = getDatas.get(i);

                String getUri = data.getItemApiUri();
                String getAppName = data.getItemId();
                Log.i("getInfo", "가져온 앱: " + getAppName + "/Uri: " + getUri);

                // 값을 저장한다.
                SharedPreferences preferences = getSharedPreferences("REGISTER_DATA", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("uri", getUri);  // com.asd.asda.youtube
                editor.putString("name", getAppName);  // Youtube
                editor.commit();

                // 브로드캐스트용 Intent
                Intent intent = new Intent();
                intent.setAction("android.intent.action.save_app");
                // 앱 선택했으니 처리할사람은 이 broadcast를 받아서 처리해!
                sendBroadcast(intent);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}