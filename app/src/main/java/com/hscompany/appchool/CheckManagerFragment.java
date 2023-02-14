package com.hscompany.appchool;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

// 디자인 패턴
public class CheckManagerFragment extends Fragment {

    Context context;
    View view;

    ListView lvCheckManager;
    ImageView ivbtnAddData;
    ImageView ivBtnDeleteData;
    AddDataDialog addDataDialog;

    RecycleDataDialog reCycleDataDialog;

    ItemData itemData;

    FirebaseFirestore firebaseFirestore;
    SharedPreferences sharedPreferences;
    String Id;
//    ListHomeAdapter adapter;

    FragmentActivity fragmentActivity;

    CheckReceiver checkReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        fragmentActivity = getActivity();

        view = inflater.inflate(R.layout.checkmanager_fragment, container, false);

        lvCheckManager = view.findViewById(R.id.lv_home_checkmanager);
        ivbtnAddData = view.findViewById(R.id.ivbtn_home_addData);

        sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        Id = sharedPreferences.getString("id", "");

        firebaseFirestore = FirebaseFirestore.getInstance();

        // adadpater객체 생성
        ListHomeAdapter adapter = new ListHomeAdapter();

        Log.i("MMF_USER", "id: " + Id);

        // 인텐트 관련으로 객체가 초기화 되지 않도록 동기 설정이 필요함.

        try {
            checkReceiver = new CheckReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.hscompnay.appchool.add_complete");
            context.registerReceiver(checkReceiver, filter);
        } catch (Exception ex) {
        }


        firebaseFirestore.collection(Id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.i("MMF_TAG", "documnt: " + document.getId() + "data: " + document.getData());

                        String appAndWeb = (String) document.getData().get("appAndWeb");
                        String content, indexes, link, appLink, title, appPackage;
                        long hour, minute;
                        boolean setTouchStatus;

                        // int는 4바이트
                        //  long 8바이트
                        hour = (long) document.getData().get("hour");
                        minute = (long) document.getData().get("minute");
                        content = (String) document.getData().get("content");
                        indexes = (String) document.getData().get("indexes");
                        link = (String) document.getData().get("link");
                        appLink = (String) document.getData().get("appLink");
                        title = (String) document.getData().get("title");
                        appPackage = (String) document.getData().get("package");
                        setTouchStatus = (boolean) document.getData().get("setTouchStatus");

//                        Log.i("MMF_USER_GET", "content: " + content +
//                                "/indexes: " + indexes +
//                                "/link: " + link +
//                                "/title: " + title +
//                                "/appPackage: " + appPackage +
//                                "/year: " + year +
//                                "/month: " + month +
//                                "/day: " + day +
//                                "/hour: " + hour +
//                                "/minute: " + minute);

                        // ItemData객체로 만들고
                        itemData = new ItemData(appAndWeb, title, link, appLink, indexes, appPackage, content, hour, minute, setTouchStatus);

                        Log.i("MMF_USER_GET", itemData.toString());

                        // 어댑터의 addItem메소드 호출
                        adapter.addItem(itemData);
                    }
                    adapter.fragmentUseCancelLine(1); // 출석관리 프래그먼트에는 취소선 적용할 수 없도록 스위치

                    lvCheckManager.setAdapter(adapter);
                }
            }
        });

        // 수정
        lvCheckManager.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                ItemData data = (ItemData) adapter.getItem(i); // adapter에서 해당 position에 맞는 data를 리턴한다.
                Log.i("gugu", "title: " + data.getTitle());
                adapter.datas.remove(i);


                // 파이어베이스에서도  삭제하기
                firebaseFirestore.collection(Id).document(data.getTitle()).delete();
                Toast.makeText(context, "삭제완료", Toast.LENGTH_SHORT).show();


                FragmentTransaction transaction = Global.fragmentManager.beginTransaction();


//        transaction.replace(R.id.fragmentContainer, new CheckManagerFragment()).commit();
                transaction.detach(CheckManagerFragment.this).attach(CheckManagerFragment.this).commit();

//                reCycleDataDialog = new RecycleDataDialog(context, CheckManagerFragment.this);
//                reCycleDataDialog.setItem(data);
//                reCycleDataDialog.callFunction();

                // 기본 정보 불러오기
//                ItemData data = new ItemData(); // 객체를 실시간으로 초기화.

                // Object: 자바의 조상 1등부모
                // ItemData: 자식

                // 부모 -> 자식 : 다운캐스팅
                // 자식 -> 부모 : 업캐스팅

//                int double
//                        double a = 3; : 업캐스팅
//                        int b = (int)3.14; : 다운캐스팅

//                boolean setTouchStatus = data.getSetTouchStatus(); // 출석완료를 의미하는 터치 가능여부.


                return false;
            }
        });

        // 추가
        ivbtnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataDialog = new AddDataDialog(context, CheckManagerFragment.this);
                addDataDialog.callFunction();
            }
        });

        return view;
    }


    public void pass() {
        if (fragmentActivity == null) {
            Log.i("TAG", "null");
        } else if (fragmentActivity != null) {
            Log.i("TAG", "not null");
        }
    }


    // 생명주기

    @Override
    public void onStart() {
        super.onStart();

        Log.i("gogo", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("gogo", "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i("gogo", "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i("gogo", "onPause");
    }

    private void refreshFragment() {
        // Fragment가 null일때, 아닐때가있는데 시점확인 불분명
        // 항상 FramgnetManager를 null아닌걸 쓰고싶어서 Global사용

        FragmentTransaction transaction = Global.fragmentManager.beginTransaction();


//        transaction.replace(R.id.fragmentContainer, new CheckManagerFragment()).commit();
        transaction.detach(this).attach(this).commit();
    }

    class CheckReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals("com.hscompnay.appchool.add_complete")) {
                // 프래그먼트 갱신
                refreshFragment();
            }
        }
    }

}
