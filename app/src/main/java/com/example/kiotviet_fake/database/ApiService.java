package com.example.kiotviet_fake.database;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("select.php")
    Call<String> getData();
}


