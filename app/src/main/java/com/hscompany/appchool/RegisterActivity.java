package com.hscompany.appchool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RegisterActivity extends AppCompatActivity {

    CheckBox cbTermOfUse; // 이용약관
    CheckBox cbPrivacyPolicy;
    EditText edtAddId; // 아이디
    EditText edtAddEmail; // 본인인증용 Email;
    EditText edtCheckPw; // 비번
    EditText edtAddPw; // 비번확인
    EditText edtCode;
    ImageView btnOk; // 가입
    TextView tvCheckIDMessage; // 문제 여부 메세지

    Spinner spnBirth;

    BroadcastReceiver termOfuseReceiver; // 이용약관 확인 관련 리시버

//    int dataDuplicationPreventionSwitch;
//    int dataDuplicationPreventionEmailSwitch;

    boolean duplicationPreventID = true; // false는 사용불가
    boolean duplicationPreventEmail = true; // false는 사용불가
    boolean duplicationPreventPW = true; // false는 사용불가

    String TAG = RegisterActivity.class.getSimpleName(); // 전체 로그

    // 뒤로가기 관련 변수
    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 연동
        cbTermOfUse = findViewById(R.id.cbox_register_termOfUse);
        cbPrivacyPolicy = findViewById(R.id.cbox_register_privacy_policy);
        edtAddId = findViewById(R.id.edt_register_addid);
        edtAddEmail = findViewById(R.id.edt_register_add_email);
        edtAddPw = findViewById(R.id.edt_register_pw);
        edtCheckPw = findViewById(R.id.edt_register_checkPw);
        edtCode = findViewById(R.id.edt_register_code);
        btnOk = findViewById(R.id.btn_register_ok);
        tvCheckIDMessage = findViewById(R.id.tv_register_check_id_message);
        spnBirth = findViewById(R.id.spin_register_birth);

        FirebaseFirestore db = FirebaseFirestore.getInstance(); // DB


        // Spinner 출생연도 구하기
        ArrayAdapter<String> aa_items = new ArrayAdapter<String>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item);
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        for (int i = 1900; i <= currentYear; i++) {
            aa_items.add(String.valueOf(i));
        }
        spnBirth.setAdapter(aa_items);

        // 이용약관 다이어로그
        cbTermOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cbTermOfUse.setChecked(false); // 초기값은 false로 유지.

                // termOfUse Dialog
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_register_term_of_use, null);

                ScrollView scContent = dialogView.findViewById(R.id.sc_dialog_register_term_of_use_content);
                ImageView btnOk = dialogView.findViewById(R.id.btn_register_termsOfDialog_ok);

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                AlertDialog dialog = builder.create();
                dialog.setView(dialogView);

                dialog.show();

                // https://stickode.tistory.com/291
                scContent.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) { // i가
                        Log.i("asdf", "i: " + i + ",i1: " + i1 + ",i2: " + i2 + "i3: " + i3);

                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // termOfUse에서 받아올 setChecked값을 BroadCast로 실기안 응답.
                        MyReceiver receiver = new MyReceiver();
                        IntentFilter filter = new IntentFilter();
                        filter.addAction("com.RegisterActivity.check");
                        registerReceiver(receiver, filter);

                        Intent intent = new Intent();
                        intent.setAction("com.RegisterActivity.check");
                        sendBroadcast(intent);

                        dialog.dismiss();
                    }
                });

            }
        });

        // 개인정보 처리 방침 다이어로그
        cbPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cbPrivacyPolicy.setChecked(false); // 초기값은 false로 유지.

                // termOfUse Dialog
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_register_privacy_policy, null);

                ScrollView scContent = dialogView.findViewById(R.id.sc_dialog_register_privacy_policy_content);
                ImageView btnOk = dialogView.findViewById(R.id.btn_register_privacy_policy_Dialog_ok);

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                AlertDialog dialog = builder.create();
                dialog.setView(dialogView);

                dialog.show();


                // https://stickode.tistory.com/291
                scContent.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View view, int i, int i1, int i2, int i3) { // i가
                        Log.i("asdf", "i: " + i + ",i1: " + i1 + ",i2: " + i2 + "i3: " + i3);

                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // termOfUse에서 받아올 setChecked값을 BroadCast로 실기안 응답.
                        MyReceiver receiver = new MyReceiver();
                        IntentFilter filter = new IntentFilter();
                        filter.addAction("com.RegisterActivity.checkPrivacy");
                        registerReceiver(receiver, filter);

                        Intent intent = new Intent();
                        intent.setAction("com.RegisterActivity.checkPrivacy");
                        sendBroadcast(intent);

                        dialog.dismiss();
                    }
                });

            }
        });


        // 버튼을 누르면 DB에 정보 전송
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = edtAddId.getText().toString().trim();
                String pw = edtAddPw.getText().toString();
                String email = edtAddEmail.getText().toString();
                String checkPw = edtCheckPw.getText().toString();
                String code = edtCode.getText().toString();
                String birth = spnBirth.getSelectedItem().toString();

                tvCheckIDMessage.setVisibility(View.GONE);

//                dataDuplicationPreventionSwitch = 0; // id 중복방지 검증값 초기화 0
//                dataDuplicationPreventionEmailSwitch = 0; // email 중복방지 검증값 초기화

                duplicationPreventID = true; // false는 사용불가
                duplicationPreventEmail = true; // false는 사용불가
                duplicationPreventPW = true; // false는 사용불가


                tvCheckIDMessage.setVisibility(View.GONE);

                if (cbTermOfUse.isChecked() == false || cbPrivacyPolicy.isChecked() == false) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // null
                            tvCheckIDMessage.setText("약관을 모두 확인해주세요.");
                            tvCheckIDMessage.setVisibility(View.VISIBLE);
                        }
                    }, 100);

                } else {
                    if (id.trim().equals("") || pw.trim().equals("") || checkPw.trim().equals("") || email.trim().equals("") || code.trim().equals("")) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // null
                                tvCheckIDMessage.setText("내용을 모두 입력해주세요.");
                                tvCheckIDMessage.setVisibility(View.VISIBLE);
                            }
                        }, 100);


                    } else { // not null

                        // 데이터를 가져오고 참거짓값을 구분한다.
                        // 1. 모든 칸이 null일 시 오류 처리
                        // 2. 중복확인이 필요한 칸이 따로 처리
                        // 3. 모든칸이 조건 허용되는 값일 때 데이터 처리(애매모호한 else 처리보다 확실한 조건을 줄 것)
                        // 그 외에는 boolean을 통한 반복문으로 값 받기 알고리즘 사용하기.

                        db.collection("userInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    // ID DB
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String dbId = document.getId();

                                        // ID 대조 서로가 일치하면 0찍고 탈출, 아니면 끝날때까지 1 순차진행형, 비 순차 시 break를 걸고 따로 또 변수가 필요하다.
                                        if (id.equals(dbId)) {
                                            duplicationPreventID = false;
                                            break; // break를 걸면 반복문 탈출
                                        }
                                    }

                                    Log.i(TAG, "pw: " + pw + "/checkpw: " + checkPw);

                                    if (!pw.equals(checkPw)) {
                                        duplicationPreventPW = false; // false는 사용불가
                                    }

                                    // Email DB
                                    for (QueryDocumentSnapshot document2 : task.getResult()) {
                                        String dbEmail = (String) document2.getData().get("email");
//                                        Log.i(TAG, "email: " + email + "/DBEmail: " + dbEmail);

                                        if (email.equals(dbEmail)) {
                                            duplicationPreventEmail = false;
                                            break;
                                        }
                                    }

                                    if (duplicationPreventID == false) {
                                        tvCheckIDMessage.setText("사용이 불가능한 ID에요.");
                                        tvCheckIDMessage.setVisibility(View.VISIBLE);

                                    } else if (duplicationPreventPW == false) {
                                        tvCheckIDMessage.setText("비밀번호가 서로 일치하지 않아요.");
                                        tvCheckIDMessage.setVisibility(View.VISIBLE);

                                    } else if (duplicationPreventEmail == false) {
                                        tvCheckIDMessage.setText("사용이 불가능한 Email이에요.");
                                        tvCheckIDMessage.setVisibility(View.VISIBLE);

                                    } else if (duplicationPreventID != false && duplicationPreventEmail != false) { // 둘다 false가 아니면
                                        tvCheckIDMessage.setVisibility(View.GONE);

                                        Map<String, Object> user = new HashMap<>();
                                        user.put("id", id);
                                        user.put("pw", sha256(pw)); // 암호화 필요
                                        user.put("email", email);
                                        user.put("failCount", "0"); // 로그인 실패 카운트
                                        user.put("code", code);
                                        user.put("birth", birth);

                                        db.collection("userInfo").document(id).set(user);

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(RegisterActivity.this, "회원가입이 완료되었어요", Toast.LENGTH_SHORT).show();

//
//                                    Log.i(TAG, "ID:" + id + "/PW:" + decodePw + "/Email:" + email);
//
//                                    Map<String, Object> user = new HashMap<>();
//                                    user.put("id", id);
//                                    user.put("pw", decodePw); // 암호화 필요
//                                    user.put("email", email);
//                                    user.put("failCount", "0"); // 로그인 실패 카운트
//
//                                    db.collection("userInfo").document(id).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//
//                                            tvCheckIDMessage.setVisibility(View.GONE);
//                                            Intent intent = new Intent(RegisterActivity.this, RegisterActivateActivity.class);
//                                            startActivity(intent); // 이메일 인증 액티비티
//                                            finish();
//                                        }
//                                    });


                                    }
                                }
                            }
                        });

//                    db.collection("userInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    String dbId = document.getId();
//                                    String dbEmail = (String) document.getData().get("email");
//
//                                    Log.i(TAG, dbId + " email: " + dbEmail);
//
//
//                                    // ID 대조 서로가 일치하면 0찍고 탈출, 아니면 끝날때까지 1 순차진행형, 비 순차 시 break를 걸고 따로 또 변수가 필요하다.
//                                    if (id.equals(dbId)) {
//                                        duplicationPreventID = false;
//                                        break; // break를 걸면 반복문 탈출
//                                    }
//
//                                    if (email.equals(dbEmail)) {
//                                        duplicationPreventEmail = false;
//                                        break;
//                                    }
//                                }
//
//                                if (duplicationPreventID == false) {
//                                    tvCheckIDMessage.setText("사용이 불가능한 ID에요.");
//                                    tvCheckIDMessage.setVisibility(View.VISIBLE);
//
//                                } else if (duplicationPreventEmail == false) {
//                                    tvCheckIDMessage.setText("사용이 불가능한 Email이에요.");
//                                    tvCheckIDMessage.setVisibility(View.VISIBLE);
//
//                                } else if (duplicationPreventID != false && duplicationPreventEmail != false) { // 둘다 false가 아니면
//                                    tvCheckIDMessage.setVisibility(View.GONE);
//                                    Toast.makeText(RegisterActivity.this, "ok!!", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    });


//                    db.collection("userInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    String dbId = document.getId();
//                                    String dbEmail = (String) document.getData().get("email");
//
//                                    // ID 대조 서로가 일치하면 0찍고 탈출, 아니면 끝날때까지 1
//                                    if (id.equals(dbId)) {
//                                        dataDuplicationPreventionSwitch = 0;
//                                        break;
//
//                                    } else if (!id.equals(dbId)) {
//                                        dataDuplicationPreventionSwitch = 1;
//                                    }
//
//                                    if (email.equals(dbEmail)) {
//                                        dataDuplicationPreventionEmailSwitch = 0;
//                                        break;
//
//                                    } else if (!email.equals(dbEmail)) {
//                                        dataDuplicationPreventionEmailSwitch = 1;
//                                    }
//                                }
//                                Log.i(TAG, "ID,Email 가능 여부: " + dataDuplicationPreventionSwitch + " " + dataDuplicationPreventionEmailSwitch);
//
//                                if (dataDuplicationPreventionSwitch == 0) {
//                                    Log.i(TAG, "ID가 중복됨.");
//                                    tvCheckIDMessage.setText("해당 이메일은 사용중이거나 사용이 불가능해요.");
//
//                                } else if (dataDuplicationPreventionEmailSwitch == 0) {
//                                    Log.i(TAG, "Email가 중복됨.");
//                                    tvCheckIDMessage.setText("해당 이메일은 사용중이거나 사용이 불가능해요.");
//
//                                } else {
//                                    Toast.makeText(RegisterActivity.this, "ok!", Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//                        }
//                    });


//                    db.collection("userInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    String dbID = document.getId();
//                                    String dbEmail = (String) document.getData().get("email");
//                                    Log.i(TAG, "dbID: " + dbID + "/ dbEmail: " + dbEmail);
//
//                                    if (id.equals(dbID)) {
//                                        tvCheckIDMessage.setText("해당 아이디는 사용중이거나 사용이 불가능해요.");
//                                        tvCheckIDMessage.setVisibility(View.VISIBLE);
//
//                                    } else if (!pw.equals(checkPw)) {
//                                        tvCheckIDMessage.setText("비밀번호가 서로 일치하지 않아요.");
//                                        tvCheckIDMessage.setVisibility(View.VISIBLE);
//
//                                    } else if (email.equals(dbEmail)) {
//                                        tvCheckIDMessage.setText("해당 이메일은 사용중이거나 사용이 불가능해요.");
//                                        tvCheckIDMessage.setVisibility(View.VISIBLE);
//
//                                    } else if (!id.equals(dbID) && pw.equals(checkPw) && !email.equals(dbEmail)) {
//
//                                        String decodePw = sha256(pw);
//
//                                        Log.i(TAG, "ID:" + id + "/PW:" + decodePw + "/Email:" + email);
//
//                                        Map<String, Object> user = new HashMap<>();
//                                        user.put("id", id);
//                                        user.put("pw", decodePw); // 암호화 필요
//                                        user.put("email", email);
//                                        user.put("failCount", "0"); // 로그인 실패 카운트
//
//                                        db.collection("userInfo").document(id).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//
//                                                tvCheckIDMessage.setVisibility(View.GONE);
//                                                Intent intent = new Intent(RegisterActivity.this, RegisterActivateActivity.class);
//                                                startActivity(intent); // 이메일 인증 액티비티
//                                                finish();
//                                            }
//                                        });
//                                    }
//                                }
//                            }
//                        }
//                    });
                    }
                }


            }
        });

//        // 수정된 데이터 불러오기 주로 값 대치시, Toast나 Text 중복 출현 방지를 위해 Boolean으로 처리한 코드.

        // db
//        int[] data = new int[5];
//        data[0] = 10;
//        data[1] = 20;
//        data[2] = 30;
//        data[3] = 40;
//        data[4] = 0;
//
//        Scanner scan = new Scanner(System.in);
//
//        int a = scan.nextInt();
//        int getdata;
//        boolean status = false;
//
//        for(int i = 0; i<data.length; i++) {
//            if(a != data[i]) {
//                status = false;
//
//            }else {
//                status = true;
//
//                getdata = data[i];
//                break;
//            }
//        }
//        if(status == true) {
//            System.out.println("사용이 가능한 번호입니다.");
//        }else {
//            System.out.println("사용 불가능한 번호입니다.");
//        }

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

//    // 뒤로 가기
//    @Override
//    public void onBackPressed() { //
//        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
//            backKeyPressedTime = System.currentTimeMillis();
//            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
//            toast.show();
//            return;
//        }
//        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
//        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
//        // 현재 표시된 Toast 취소
//        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
//            finish();
//            toast.cancel();
//        }
//    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "MyReceiver");
            Log.i(TAG, intent.getAction());

            if (action.equals("com.RegisterActivity.checkPrivacy")) {
                cbPrivacyPolicy.setChecked(true);
            } else {
                cbTermOfUse.setChecked(true);
            }
        }
    }
}