package com.hscompany.appchool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class GetAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_account);

        TextView tvGetID = findViewById(R.id.tv_get_account_id);
        TextView tvGetCode = findViewById(R.id.tv_get_account_code);
        ImageView btnFinish = findViewById(R.id.btn_get_account_finish);

        Intent intent = getIntent();

        tvGetID.setText("ID는 " + intent.getStringExtra("ID"));
        tvGetCode.setText("인증번호는 " + intent.getStringExtra("code") + "입니다");

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetAccountActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 호출하려는 액티비티가 남아있으면 그 액티비티만 가고, 나머지 모두 삭제.
                // https://dduntorry.tistory.com/entry/Activity-%EC%8A%A4%ED%83%9D-%EC%A0%9C%EA%B1%B0
                // setFlags 항상 Intent는 this와 class 방향만 적용됩니다.
                // NEW_TASK는 다음 새 액티비티르 생성, CLEAR_TASK는 현재 액티비티를 삭제, CLEAR_TOP은 기존에 생성되었던 한 액티비티를 그대로 유지(class로)
                // 더 쉽게 말하면, 호출하려고 하는 액티비티가 이미 스택에 쌓여 있으면 스택에 있는 그 액티비티로 가져온다. 그리고 그 이전의 포 그라운드를 모두 삭제?
                startActivity(intent);
                finish();
            }
        });
    }
}