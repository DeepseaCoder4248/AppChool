package com.hscompany.appchool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.Image;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edtLoginid; // id
    EditText edtLoginpw; // pw
    ImageView ibtnLogin; // login
    ImageView btnLoginGoogle; // GoogleAccount
    TextView tvLoginRegister; // 회원가입
    TextView tvLoginSearchAccount; // 아디 비번 찿기
    TextView tvErrorMessage;

    String failCount = "";
    int convertCount = 0;
    int loginFlag = 0;

    // DB
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // FireStore
    SharedPreferences sharedPreferences; // sharedFreferences
    String dbID;
    String dbPW;

    String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLoginid = findViewById(R.id.edt_login_id);
        edtLoginpw = findViewById(R.id.edt_login_pw);
        tvLoginRegister = findViewById(R.id.tv_register);
        tvLoginSearchAccount = findViewById(R.id.tv_searchAccount);
        ibtnLogin = findViewById(R.id.btn_login);
        tvErrorMessage = findViewById(R.id.tv_login_error_message);

        // 자동 로그인과 일반 로그인 여부
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE); // db 생성.
        String autoLoginStatus = sharedPreferences.getString("autoLoginStatus", "0"); // 자동 로그인 여부, 0이면 쌩폰이다.
        String autoLoginID = sharedPreferences.getString("autoLoginID", "no"); // 자동 로그인 ID 여부, no면 쌩폰이다.

        // 자동 로그인 검증, 1이면 자동 로그인.
        if (autoLoginStatus.equals("1")) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("id", autoLoginID);
            startActivity(intent);
            finish();
        }

//        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

        // 로그인
        ibtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1.입력한 정보 가져오기
                String id = edtLoginid.getText().toString(); // id 가져오기
                String tempPw = edtLoginpw.getText().toString();
                String pw = sha256(edtLoginpw.getText().toString()); // pw 가져오기

                Log.i(TAG, "Login Data: " + "id: " + id + "/pw: " + pw);
                Log.i(TAG, "idlength: " + id.length() + "pwtemplength: " + tempPw.length());

                if (id.length() == 0 || tempPw.length() == 0) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            errorMessage("내용을 모두 입력해주세요");
                        }
                    }, 100);

                } else {

                    db.collection("userInfo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                int userCount = task.getResult().size();
                                if (userCount == 0) {
                                    return;
                                }

                                for (QueryDocumentSnapshot snapshot : task.getResult()) {  // 1번하면 0넣고 1, 2번하면 1넣고 2

                                    // 멤버변수 초기화
                                    failCount = "";
                                    convertCount = 0;
                                    loginFlag = 0;

                                    // ID,PW,FailCount
                                    dbID = snapshot.getId();
                                    dbPW = (String) snapshot.getData().get("pw");
                                    failCount = (String) snapshot.getData().get("failCount"); // failCount
                                    convertCount = Integer.parseInt(failCount); // int로 바꿔줄 컨버터

                                    Log.i(TAG, "ID: " + dbID + ",failcount: " + failCount);

                                    if (!id.equals(dbID) && !pw.equals(dbPW)) { // 아디 비번 모두 틀린 경우
                                        loginFlag = 0;

                                        Log.i(TAG, "DE,DE");

                                    } else if (id.equals(dbID) && !pw.equals(dbPW)) { // 아디는 맞는데 비번이 틀린 경우
                                        convertCount = convertCount + 1;
                                        loginFlag = 1;

                                        Log.i(TAG, "PA,DE");
                                        break;


                                    } else if (id.equals(dbID) && pw.equals(dbPW)) { // 아디 비번이 모두 맞은 경우

                                        if (convertCount > 4) {
                                            loginFlag = 1;

                                        } else {
                                            loginFlag = 2;
                                        }

                                        Log.i(TAG, "PA,PA");
                                        break;
                                    }
                                }

                                // --for문 탈출 구역 ------------------------------------------
                                Log.i(TAG, "LoginFlag is: " + loginFlag);

                                Log.i(TAG, "아디:" + id);

                                switch (loginFlag) {
                                    case 0:
                                        errorMessage("로그인 정보가 일치하지 않습니다. 다시 입력해주세요");
                                        break;

                                    case 1:

                                        if (convertCount > 4) { // 5
                                            db.collection("userInfo").document(id).update("failCount", String.valueOf(convertCount)); // 햇갈림 방지, db에서는 4에서 멈춘.
                                            errorMessage("해당 ID는 5회 이상 틀려 로그인이 불가능합니다. 고객센터에 연락해주세요");

                                        } else if (convertCount < 5) { // 4
                                            db.collection("userInfo").document(id).update("failCount", String.valueOf(convertCount));

                                            errorMessage("비밀번호 오류가 " + convertCount + "회 이상입니다.." + "5회 이상시 로그인이 불가능해요");
                                        }
                                        break;

                                    case 2:

                                        db.collection("userInfo").document(id).update("failCount", "0");

                                        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("id", dbID); // User / id / Id 객체
                                        editor.commit();

                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                        finish();
                                }
                            }
                        }
                    });
                }
            }
        });

        // 회원가입
        tvLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvLoginSearchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AccountInformationActivity.class);
                startActivity(intent);

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

    public static String getKeyHash(final Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null)
                return null;

            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //public void callvack(){
    // Callback callback = new CAllback();
    // callback.onFailure()
    // 확실하게 다시 알아야 할 듯.

    public Callback callback = new Callback() { // 익명 클래스로 추정. 익명클래스는 무조건 Implement하는 듯. 그리고 implement된 callback은 클래스로 사용이 가능한가?
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.i(TAG, "KakaoCall: " + "failed");
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String body = response.toString(); // 정보 받아오기
            Log.i("TAG", body);

//            //            Pattern pattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
//            String regex = "^(https):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$";
//            String[] url = body.split(regex);
//            String a = url[0];
//            Log.i("asdf", a);

            String getUrl = extractUrl(body); // group은 String을 Return


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl)); // uri로 실행하는 Intent 코드
            startActivity(intent);
        }
    };

    // 출처: https://jizard.tistory.com/237 [Lou]
    public static String extractUrl(String content) {
        try {
            String REGEX = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
            Pattern p = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(content);
            if (m.find()) {
                return m.group();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public void errorMessage(String message) {
        tvErrorMessage.setText(message); // 보안으로 인해 하나라도 틀리면 모두가 틀린 것처럼 해야 함.
        tvErrorMessage.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvErrorMessage.setVisibility(View.GONE);
            }
        }, 3000);
    }
}