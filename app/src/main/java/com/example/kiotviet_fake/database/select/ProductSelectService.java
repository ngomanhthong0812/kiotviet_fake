package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductSelectService {
    @GET("products/select.php")
    Call<String> getProducts();
}
