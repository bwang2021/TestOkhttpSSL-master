package com.example.testokhttp;

import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLUtil {
    public static SSLSocketFactory getSSLSocketFactory(Context context) throws Exception {
        String clientPass = "kRw8EdZ5WdBtQ35ENPCV4Q==";
        String clientTrustKeyStorePassword = "Ykhl2023==";

        // 加载客户端证书
        InputStream clientInput = context.getResources().openRawResource(R.raw.client);

        // 加载服务器证书
        InputStream serverInput = context.getResources().openRawResource(R.raw.server);

        // 加载客户端证书和私钥
        KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        clientKeyStore.load(clientInput, clientPass.toCharArray());

        // 加载服务器证书
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(serverInput, clientTrustKeyStorePassword.toCharArray());


        // 初始化 KeyManagerFactory
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientKeyStore, clientPass.toCharArray());

        // 初始化 TrustManagerFactory
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };


        SSLContext sslContext = SSLContext.getInstance("TLS");

        sslContext.init(keyManagerFactory.getKeyManagers(),trustAllCerts, new SecureRandom());

        return sslContext.getSocketFactory();
    }
}
