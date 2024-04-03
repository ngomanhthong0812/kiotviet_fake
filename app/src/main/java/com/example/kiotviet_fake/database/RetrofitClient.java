package com.example.kiotviet_fake.database;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://ngomanhthong-001-site1.htempurl.com/tables/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(String username, String password) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new BasicAuthInterceptor(username, password))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}


