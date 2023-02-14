package com.hscompany.appchool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SettingsUserInfo extends AppCompatActivity {

    ListView lvUserInfo;
    SettingsUserInfoAdapter adapter;
    LinearLayout changePWViewGroup;
    SettingsDeleteUserDialog settingsDeleteUserDialog;

    FirebaseFirestore firestore;
    SharedPreferences sharedPreferences;
    String ID;
    String DBEmail;

    int PWflag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_user_info2);

        lvUserInfo = findViewById(R.id.lv_settings_userInfo);
        changePWViewGroup = findViewById(R.id.viewgroup_settings_user_info);

        adapter = new SettingsUserInfoAdapter();
        firestore = FirebaseFirestore.getInstance();

        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        ID = sharedPreferences.getString("id", "null");

        SettingsUserInfoItem IDdata = new SettingsUserInfoItem("아이디", ID); // 0
        adapter.addItem(IDdata);

        firestore.collection("userInfo").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                DBEmail = (String) snapshot.getData().get("email");

                SettingsUserInfoItem EmailData = new SettingsUserInfoItem("이메일", DBEmail); // 0
                adapter.addItem(EmailData);

                SettingsUserInfoItem changePW = new SettingsUserInfoItem("비밀번호 변경", ""); // 1
                adapter.addItem(changePW);

                SettingsUserInfoItem deleteUser = new SettingsUserInfoItem("회원탈퇴", ""); // 2
                adapter.addItem(deleteUser);

                lvUserInfo.setAdapter(adapter);
            }
        });

        lvUserInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {

                    case 0:
                        break;

                    case 1:
                        break;

                    case 2:

                        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsUserInfo.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View builderView = inflater.inflate(R.layout.settings_user_info_change_pw, changePWViewGroup, false);

                        TextView tvPW = builderView.findViewById(R.id.tv_settings_user_info_pw);
                        TextView tvCheckPW = builderView.findViewById(R.id.tv_settings_user_info_checkpw);
                        TextView tvMessage = builderView.findViewById(R.id.tv_settings_user_info_message);
                        ImageView btnOk = builderView.findViewById(R.id.btn_settings_user_info_ok);

                        AlertDialog dialog = builder.create();
                        dialog.setView(builderView);
                        dialog.show();

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                tvMessage.setVisibility(View.GONE);

                                String PW = tvPW.getText().toString();
                                String checkPW = tvCheckPW.getText().toString();

                                if (PW.trim().equals("") || checkPW.trim().equals("")) {
                                    message(builderView, tvMessage, "내용을 모두 입력해주세요");

                                } else if (!PW.trim().equals(checkPW)) {
                                    message(builderView, tvMessage, "비빌번호가 서로 일치하지 않아요");


                                } else if (PW.trim().equals(checkPW)) {

                                    PWflag = 0;

                                    firestore.collection("userInfo").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot snapshot = task.getResult();
                                                String dbPW = (String) snapshot.getData().get("pw");
                                                // Qurey는 collection에서 다 가져와야 하기에 for를 쓴 것이고, document가 있으면 바로 snapshot 불러오면 된다. 비번 변경시 필요.

                                                if (sha256(PW).equals(dbPW)) {
                                                    message(builderView, tvMessage, "입력하신 비밀번호는 사용할 수 없어요");

                                                } else {
                                                    firestore.collection("userInfo").document(ID).update("pw", sha256(PW));
                                                    Toast.makeText(SettingsUserInfo.this, "비밀번호 변경이 완료되었어요", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        break;

                    case 3:
                        settingsDeleteUserDialog = new SettingsDeleteUserDialog(SettingsUserInfo.this);
                        break;
                }
            }
        });
    }

    // 로그인 할 때 가져오는 PW는 SHA-256
    public static String sha256(String str) {
        String SHA = "";
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++)
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            SHA = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
    }

    public void message(View view, TextView textView, String message) {
        View getView = view;
        TextView getTextView = textView;

        getTextView = getView.findViewById(R.id.tv_settings_user_info_message);

        TextView finalGetTextView = getTextView;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finalGetTextView.setText(message);
                finalGetTextView.setVisibility(View.VISIBLE);
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}