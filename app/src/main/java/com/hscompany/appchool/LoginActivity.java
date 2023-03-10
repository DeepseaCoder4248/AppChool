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
    TextView tvLoginRegister; // ????????????
    TextView tvLoginSearchAccount; // ?????? ?????? ??????
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

        // ?????? ???????????? ?????? ????????? ??????
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE); // db ??????.
        String autoLoginStatus = sharedPreferences.getString("autoLoginStatus", "0"); // ?????? ????????? ??????, 0?????? ????????????.
        String autoLoginID = sharedPreferences.getString("autoLoginID", "no"); // ?????? ????????? ID ??????, no??? ????????????.

        // ?????? ????????? ??????, 1?????? ?????? ?????????.
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

        // ?????????
        ibtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1.????????? ?????? ????????????
                String id = edtLoginid.getText().toString(); // id ????????????
                String tempPw = edtLoginpw.getText().toString();
                String pw = sha256(edtLoginpw.getText().toString()); // pw ????????????

                Log.i(TAG, "Login Data: " + "id: " + id + "/pw: " + pw);
                Log.i(TAG, "idlength: " + id.length() + "pwtemplength: " + tempPw.length());

                if (id.length() == 0 || tempPw.length() == 0) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            errorMessage("????????? ?????? ??????????????????");
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

                                for (QueryDocumentSnapshot snapshot : task.getResult()) {  // 1????????? 0?????? 1, 2????????? 1?????? 2

                                    // ???????????? ?????????
                                    failCount = "";
                                    convertCount = 0;
                                    loginFlag = 0;

                                    // ID,PW,FailCount
                                    dbID = snapshot.getId();
                                    dbPW = (String) snapshot.getData().get("pw");
                                    failCount = (String) snapshot.getData().get("failCount"); // failCount
                                    convertCount = Integer.parseInt(failCount); // int??? ????????? ?????????

                                    Log.i(TAG, "ID: " + dbID + ",failcount: " + failCount);

                                    if (!id.equals(dbID) && !pw.equals(dbPW)) { // ?????? ?????? ?????? ?????? ??????
                                        loginFlag = 0;

                                        Log.i(TAG, "DE,DE");

                                    } else if (id.equals(dbID) && !pw.equals(dbPW)) { // ????????? ????????? ????????? ?????? ??????
                                        convertCount = convertCount + 1;
                                        loginFlag = 1;

                                        Log.i(TAG, "PA,DE");
                                        break;


                                    } else if (id.equals(dbID) && pw.equals(dbPW)) { // ?????? ????????? ?????? ?????? ??????

                                        if (convertCount > 4) {
                                            loginFlag = 1;

                                        } else {
                                            loginFlag = 2;
                                        }

                                        Log.i(TAG, "PA,PA");
                                        break;
                                    }
                                }

                                // --for??? ?????? ?????? ------------------------------------------
                                Log.i(TAG, "LoginFlag is: " + loginFlag);

                                Log.i(TAG, "??????:" + id);

                                switch (loginFlag) {
                                    case 0:
                                        errorMessage("????????? ????????? ???????????? ????????????. ?????? ??????????????????");
                                        break;

                                    case 1:

                                        if (convertCount > 4) { // 5
                                            db.collection("userInfo").document(id).update("failCount", String.valueOf(convertCount)); // ????????? ??????, db????????? 4?????? ??????.
                                            errorMessage("?????? ID??? 5??? ?????? ?????? ???????????? ??????????????????. ??????????????? ??????????????????");

                                        } else if (convertCount < 5) { // 4
                                            db.collection("userInfo").document(id).update("failCount", String.valueOf(convertCount));

                                            errorMessage("???????????? ????????? " + convertCount + "??? ???????????????.." + "5??? ????????? ???????????? ???????????????");
                                        }
                                        break;

                                    case 2:

                                        db.collection("userInfo").document(id).update("failCount", "0");

                                        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("id", dbID); // User / id / Id ??????
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

        // ????????????
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

    // ????????? ??? ??? ???????????? PW??? SHA-256
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
    // ???????????? ?????? ????????? ??? ???.

    public Callback callback = new Callback() { // ?????? ???????????? ??????. ?????????????????? ????????? Implement?????? ???. ????????? implement??? callback??? ???????????? ????????? ?????????????
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.i(TAG, "KakaoCall: " + "failed");
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            String body = response.toString(); // ?????? ????????????
            Log.i("TAG", body);

//            //            Pattern pattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$");
//            String regex = "^(https):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$";
//            String[] url = body.split(regex);
//            String a = url[0];
//            Log.i("asdf", a);

            String getUrl = extractUrl(body); // group??? String??? Return


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl)); // uri??? ???????????? Intent ??????
            startActivity(intent);
        }
    };

    // ??????: https://jizard.tistory.com/237 [Lou]
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
        tvErrorMessage.setText(message); // ???????????? ?????? ???????????? ????????? ????????? ?????? ????????? ?????? ???.
        tvErrorMessage.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvErrorMessage.setVisibility(View.GONE);
            }
        }, 3000);
    }
}