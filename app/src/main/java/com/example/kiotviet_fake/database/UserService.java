package com.example.kiotviet_fake.database;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("users/select.php")
    Call<String> getUsers();
}
