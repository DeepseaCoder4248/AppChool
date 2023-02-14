package com.hscompany.appchool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AccountInformationActivity extends AppCompatActivity {

    ImageView btnFindID;
    ImageView btnFindPW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);

        btnFindID = findViewById(R.id.btn_accountInformation_find_id);
        btnFindPW = findViewById(R.id.btn_accountInformation_find_pw);

        btnFindID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountInformationActivity.this, FindAccountActivity.class);
                intent.putExtra("value", "id");
                startActivity(intent);
                finish();

            }
        });

        btnFindPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountInformationActivity.this, FindAccountActivity.class);
                intent.putExtra("value", "pw");
                startActivity(intent);
                finish();

            }
        });
    }
}