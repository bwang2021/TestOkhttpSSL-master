package com.example.testokhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient.Builder()
                            .hostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String s, SSLSession sslSession) {
                                    return true;
                                }
                            })
                            .sslSocketFactory(SSLUtil.getSSLSocketFactory(MainActivity.this))
                            .build();
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody requestBody = RequestBody.create(new Gson().toJson("{\"fid\":\"HL1048\",\"params\":{\"BAH\":\"16507991\"}}"),mediaType);

                    // 创建 Request 对象
                    Request request = new Request.Builder()
                            .url("https://esb2.z2web1.com:8013/rest-service/1602358066806816/1588916027129888")
                            .post(requestBody)
                            .build();

                    // 发起请求
                    Response response = client.newCall(request).execute();

                    // 处理响应
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        Log.d("MainActivity", "Response: " + responseBody);
                    } else {
                        Log.e("MainActivity", "Error: " + response.code() + " " + response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MainActivity", "Request failed: " + e.getMessage());
                }
            }
        }).start();

    }
}
