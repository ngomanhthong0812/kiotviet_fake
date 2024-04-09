package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HistoryService {
    @GET("bills/history.php")
    Call<String> getHistory();
}
