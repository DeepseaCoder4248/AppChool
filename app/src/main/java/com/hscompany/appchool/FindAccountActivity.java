package com.hscompany.appchool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class FindAccountActivity extends AppCompatActivity {

    LinearLayout layID;
    EditText edtEmail;
    ImageView btnNextID;

    LinearLayout layPW;
    EditText edtCode;
    EditText edtID;
    EditText edtResetPW;
    EditText edtResetCheckPW;
    ImageView btnNextPW;

    String email;
    String dbEmail;
    String dbID;
    String dbCode;
    String dbPW;
    String dbCheckPW;
    int emailFlag = 0; // 0은 아무것도 없음. 1은 있음.
    int resetPWFlag = 0;

    TextView tvIDMessage;
    TextView tvPWMessage;

    FirebaseFirestore firebaseFirestore;

    private static Toast sToast;

    String TAG = FindAccountActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        Intent intent = getIntent();
        String value = intent.getStringExtra("value");

        // id 라인
        layID = findViewById(R.id.ly_find_account_id);
        edtEmail = findViewById(R.id.edt_find_account_put_email);
        btnNextID = findViewById(R.id.btn_find_account_next_id);
        tvIDMessage = findViewById(R.id.tv_find_account_id_message);

        // pw 라인
        layPW = findViewById(R.id.ly_find_account_pw);
        edtID = findViewById(R.id.edt_find_account_put_id);
        edtCode = findViewById(R.id.edt_find_account_put_code);
        edtResetPW = findViewById(R.id.edt_find_account_put_resetPW);
        edtResetCheckPW = findViewById(R.id.edt_find_account_put_resetCheckPW);
        btnNextPW = findViewById(R.id.btn_find_account_next_pw);
        tvPWMessage = findViewById(R.id.tv_find_account_pw_message);

        firebaseFirestore = FirebaseFirestore.getInstance();

        // value가 id면 id 모드
        if (value.equals("id")) {
            layID.setVisibility(View.VISIBLE);
            layPW.setVisibility(View.GONE);

            btnNextID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    email = edtEmail.getText().toString();
                    emailFlag = 0;
                    tvIDMessage.setVisibility(View.GONE);

                    // null 여부
                    if (email.trim().equals("")) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tvIDMessage.setVisibility(View.VISIBLE);
                                tvIDMessage.setText("내용을 모두 입력해주세요");
                            }
                        }, 100);


                    } else {
                        firebaseFirestore.collection("userInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        dbEmail = (String) snapshot.getData().get("email");
                                        dbID = (String) snapshot.getData().get("id");
                                        dbCode = (String) snapshot.getData().get("code");
                                        Log.i(TAG, "email: " + dbEmail + "/id: " + dbID + "/code: " + dbCode);

                                        if (email.equals(dbEmail)) {
                                            emailFlag = 1;
                                            break;

                                        } else {
                                            emailFlag = 0;
                                        }
                                    }

                                    if (emailFlag == 0) {
                                        tvIDMessage.setVisibility(View.VISIBLE);
                                        tvIDMessage.setText("해당되는 이메일을 찿을 수 없어요");

                                    } else if (emailFlag == 1) {
                                        tvIDMessage.setVisibility(View.GONE);
                                        tvIDMessage.setText("");

                                        Intent intent = new Intent(FindAccountActivity.this, GetAccountActivity.class);
                                        intent.putExtra("ID", dbID);
                                        intent.putExtra("code", dbCode);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        });
                    }
                }
            });

            // value가 pw면 pw 모드
        } else if (value.equals("pw")) {
            layID.setVisibility(View.GONE);
            layPW.setVisibility(View.VISIBLE);


            btnNextPW.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ID = edtID.getText().toString();
                    String code = edtCode.getText().toString();
                    String resetPW = edtResetPW.getText().toString();
                    String resetCheckPW = edtResetCheckPW.getText().toString();
                    tvPWMessage.setVisibility(View.GONE);
                    tvPWMessage.setText("");

//                    sToast = null;

                    // null 여부
                    if (ID.trim().trim().equals("") || resetPW.trim().equals("") || resetCheckPW.trim().equals("") || code.trim().equals("")) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tvPWMessage.setVisibility(View.VISIBLE);
                                tvPWMessage.setText("내용을 모두 입력해주세요");
                            }
                        }, 100);

                    } else {
                        firebaseFirestore.collection("userInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        dbID = snapshot.getId();
                                        dbPW = (String) snapshot.getData().get("pw");
                                        dbCode = (String) snapshot.getData().get("code");

                                        if (!ID.equals(dbID) || !code.equals(dbCode)) { // 한개라도 틀리면
                                            resetPWFlag = 0;

                                        } else if (ID.equals(dbID) && code.equals(dbCode)) { // 모두 맞으면
                                            resetPWFlag = 1;
                                            break;
                                        }
                                    } // 항상 플래그를 남겨서 할것.!

                                    if (resetPWFlag == 0) {
                                        tvPWMessage.setVisibility(View.VISIBLE);
                                        tvPWMessage.setText("아이디 또는 인증번호가 달라요");

                                    } else if (resetPWFlag == 1 && !resetPW.equals(resetCheckPW)) {
                                        tvPWMessage.setVisibility(View.VISIBLE);
                                        tvPWMessage.setText("비밀번호가 서로 일치하지 않아요");

                                    } else if (resetPWFlag == 1 && resetPW.equals(resetCheckPW)) {

                                        firebaseFirestore.collection("userInfo").document(ID).update("pw", sha256(resetPW));

                                        Intent intent = new Intent(FindAccountActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });


//                        firebaseFirestore.collection("userInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
//                                        String dbID = snapshot.getId();
//
//                                        if (dbID.equals(ID)) {
//
//                                            if (resetPW.equals(resetCheckPW)) {
//                                                Map<String, Object> resetData = new HashMap<>();
//                                                resetData.put("pw", sha256(resetPW));
//                                                firebaseFirestore.collection("userInfo").document(ID).update(resetData).
//                                                        addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                Log.i(TAG, "resetData is completed");
//                                                            }
//                                                        });
//
//                                                Intent intent = new Intent(FindAccountActivity.this, LoginActivity.class);
//                                                startActivity(intent);
//                                                finish();
//
//                                            } else {
//                                                showToast(FindAccountActivity.this, "비밀번호가 서로 일치하지 않아요");
//                                            }
//
//                                        } else {
//                                            showToast(FindAccountActivity.this, "해당되는 아이디를 찿을 수 없어요");
//                                            // 일단 이렇게하고 다음에는 MAP 또는 LIST 또는 배열 또는 ARRAYLIST를 사용해서 받아오는 방식.
//                                        }
//                                    }
//                                }
//                            }
//                        });
                    }
                }
            });
        }
    }

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

    // https://anhana.tistory.com/19
    public static void showToast(Context context, String message) {
        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }
}