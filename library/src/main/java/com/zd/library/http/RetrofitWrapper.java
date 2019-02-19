package com.zd.library.http;


import com.zd.library.BuildConfig;

import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * =====================================
 * 上海众朵信息科技有限公司 研发中心
 * 日期：2019/2/19 上午7:23
 *
 * @author ：junlyuan
 * 版本：1.0
 * 功能描述：Retrofit工具类
 * <p>
 * 修订日期      修订人        描述
 * 2019/2/19     junlyuan
 * ======================================
 */
public class RetrofitWrapper {

    private static volatile  RetrofitWrapper mInstance;
    private final OkHttpClient.Builder builder;

    private Retrofit mRetrofit;

    public static RetrofitWrapper getInstance(String url){
        if (mInstance == null) {
            synchronized (RetrofitWrapper.class){
                if (mInstance == null) {
                    mInstance = new RetrofitWrapper(url);
                }
            }
        }
        return mInstance;
    }

    private RetrofitWrapper(String url) {
        builder = new OkHttpClient.Builder();
        //日志拦截
        builder.addInterceptor(InterceptorUtils.getHttpLoggingInterceptor(BuildConfig.DEBUG));
        //请求头拦截器
//        builder.addInterceptor(InterceptorUtils.getRequestHeader());
        //统一请求拦截器
//        builder.addInterceptor(InterceptorUtils.commonParamsInterceptor());
        //网络拦截器
        builder.addInterceptor(InterceptorUtils.addNetWorkInterceptor());
        // 设置缓存
        builder.addInterceptor(InterceptorUtils.getCacheInterceptor());

        // 添加自定义的cookieJar
        InterceptorUtils.addCookie(builder);

        OkHttpClient build = builder.build();

        initSSL();
        initTimeOut();

        mRetrofit = new Retrofit
                .Builder()
                //设置baseUrl
                .baseUrl(url)
                //Gson转换 将json 转为JavaBean （参考）
                .addConverterFactory(GsonConverterFactory.create(JsonUtils.getJson()))
                //添加rx转换器，用来生成对应"Call"的CallAdapter的CallAdapterFactory
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(build)
                .build();
    }

    /**
     * 初始化完全信任的信任管理器
     */
    @SuppressWarnings("deprecation")
    private void initSSL() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTimeOut() {
        builder.readTimeout(20000, TimeUnit.SECONDS);
        builder.writeTimeout(20000, TimeUnit.SECONDS);
        builder.connectTimeout(10000, TimeUnit.SECONDS);
        // 错误重连
        builder.retryOnConnectionFailure(true);
    }

    public <T> T create(final Class<T> service){
        return mRetrofit.create(service);
    }
}
