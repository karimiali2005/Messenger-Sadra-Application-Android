package com.Sadraafzar.Messenger.WebService;


import com.Sadraafzar.Messenger.Classes.app;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

public class APIClient {
    public static   final String BASE_URL = app.Communication.baseUrlService ;

    private static Retrofit retrofit = null ;

    public static Retrofit getClient(String apiVersion) {

        if (retrofit == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    String str= app.AesEncryptionAlgorithm.EncryptServicePrivateKeySend(app.Info.PrivateKey);
                    str=str.substring(0,str.length()-1);
                    Request request = chain.request().newBuilder().addHeader("Authorize", str).addHeader("User", String.valueOf(app.Info.UserID)).build();
                    return chain.proceed(request);
                }
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL+apiVersion)
                    .addConverterFactory(JodaDateTimeConverterFactory.create())
                    .client(httpClient.build())
                    .build() ;
        }
        return retrofit ;
    }



}
