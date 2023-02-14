package com.hscompany.appchool;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsDeleteUserDialog {

    Dialog dlg;

    EditText edtCheckPW;
    EditText edtCheckPW2;
    EditText edtContent;
    TextView tvCurrentCount;
    TextView tvLimitCount;
    ImageView btnOk;

    int size = 0;

    SharedPreferences sharedPreferences;
    FirebaseFirestore firebaseFirestore;

    SettingsDeleteUserDialog(Context context) {
        dlg = new Dialog(context);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.settings_delete_user_dialog);

        edtCheckPW = dlg.findViewById(R.id.edt_settings_delete_check_pw);
        edtCheckPW2 = dlg.findViewById(R.id.edt_settings_delete_check_pw2);
        edtContent = dlg.findViewById(R.id.edt_settings_delete_content);
        tvCurrentCount = dlg.findViewById(R.id.tv_settings_delete_current_count);
        tvLimitCount = dlg.findViewById(R.id.tv_settings_delete_limit_count);
        btnOk = dlg.findViewById(R.id.btn_settings_delete_ok);

        // 현재 입력되는 글자수 가져오기
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 텍스트 입력이 모두 끝났을때

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 텍스트가 변경될때마다
                String content = edtContent.getText().toString();
                size = content.length();

                tvCurrentCount.setText("" + size);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 텍스트 입력하기 전에
//                Log.i("gugu_after", "after");
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1) 로그인 된 정보를 가져온다.
                // 2) db에서 userInfo의 정보를 가져온다.
                // 3) db의 id와 로그인된(shared)와 일치하면, 해당 id를 삭제한다(delete)
                // 액티비티 벗어난다.

                sharedPreferences = context.getSharedPreferences("USER", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String id = sharedPreferences.getString("id", "no");

                firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("userInfo").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context,"정상적으로 회원이 탈퇴되었습니다. 이용해주셔서 감사합니다.",Toast.LENGTH_SHORT).show();

                        editor.clear();
                        editor.commit();

                        Intent i = new Intent(context, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ((Activity)context).startActivity(i);
                        ((Activity)context).finish();
                    }
                });

                // 입력한 내용은 메일, DB 선택
            }
        });

        dlg.show();

    }

}
