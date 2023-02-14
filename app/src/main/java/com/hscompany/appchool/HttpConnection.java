package com.hscompany.appchool;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.http2.Http2Connection;

public class HttpConnection {

    // client는 실질적으로 http를 접속관리, HttpConnection은 외부에서 이 client를 불러와서 사용하기 위한 인스턴스 객체. 인스턴스화하면 메소드만 불러도 호출이 가능한듯 하다.
    private OkHttpClient client; // private? 외부에서 불러올 수 없는 것인가?
    public static HttpConnection instance = new HttpConnection(); // 외부에서 불러오는건 public이지만 static은 무엇일까?

    public static HttpConnection getInstance() { // public은 외부에서 불러오지만 stactic은 무엇일까?
        return instance;
    }

    private HttpConnection() { // 이건 무슨 문법인가요?
        this.client = new OkHttpClient();
    }

    public void request(Callback callback) {
        Request requestBuilder = new Request.Builder().url("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=5d702175206552df01bb72a3ce110df4&redirect_uri=http://www.illion.co.kr\n").build();
        client.newCall(requestBuilder).enqueue(callback);
        // https://hongsii.github.io/2017/08/02/what-is-the-difference-get-and-post/
    }
}
