package com.example.kiotviet_fake.database;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TableService {
    @GET("tables/select.php")
    Call<String> getTable();
}


