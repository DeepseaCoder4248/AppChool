package com.hscompany.appchool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterActivateActivity extends AppCompatActivity {

    TextView tvMessage;
    EditText edtAddCode;
    ImageView btnOk;
    Spinner spinner;
    EditText edtNum1;
    EditText edtNum2;
    ArrayAdapter<CharSequence> spinnerAdapter;
    ImageView btnAccess;
    FirebaseAuth auth;

    String TAG = RegisterActivateActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activate);

        tvMessage = findViewById(R.id.tv_register_activate_message);
        edtAddCode = findViewById(R.id.edt_register_activate_code);
        btnOk = findViewById(R.id.btn_register_activate_ok);
        btnAccess = findViewById(R.id.btn_register_activate_access);
        edtNum1 = findViewById(R.id.edt_register_activate_num1);
        edtNum2 = findViewById(R.id.edt_register_activate_num2);
        spinner = findViewById(R.id.spinner);

        auth = FirebaseAuth.getInstance();

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.phone_numbers, R.layout.row_spinner);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Intent intent = getIntent();
        String ID = intent.getStringExtra("ID");
        String PW = intent.getStringExtra("PW");
        String Email = intent.getStringExtra("Email");
        String failCount = intent.getStringExtra("failCount");
        String code = intent.getStringExtra("code");
        String birth = intent.getStringExtra("birth");
        Log.i(TAG, "ID:" + ID + "/PW:" + PW + "/Email:" + Email + "/ failCount: " + failCount + "/ code: " + code + "/birth: " + birth);


        btnAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // custom view 공부해보기

                String num0 = spinner.getSelectedItem().toString();
                String num1 = edtNum1.getText().toString();
                String num2 = edtNum2.getText().toString();
                String phoneNumber = "+82" + num0 + num1 + num2;
                Log.i(TAG, "phoneNumber: " + phoneNumber);


                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(auth)
                                .setPhoneNumber("+16505553434")       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(RegisterActivateActivity.this)                 // Activity (for callback binding)
                                .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);


            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String activeCode = edtAddCode.getText().toString(); // 인증 코드 가져오기.

                // 인증관련 확인 코드가 필요함.

                FirebaseFirestore firestore = FirebaseFirestore.getInstance();

                Map<String, Object> user = new HashMap<>();
                user.put("id", ID);
                user.put("pw", PW); // 암호화 필요
                user.put("email", Email);
                user.put("failCount", "0"); // 로그인 실패 카운트
                user.put("code", code);
                user.put("birth", birth);

                firestore.collection("userInfo").document(ID).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        tvMessage.setVisibility(View.GONE);
                        Intent intent = new Intent(RegisterActivateActivity.this, LoginActivity.class);
                        startActivity(intent); // 이메일 인증 액티비티
                        finish();
                    }
                });
            }
        });

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.i(TAG, "성공");
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Log.i(TAG, "실패");
        }
    };
}