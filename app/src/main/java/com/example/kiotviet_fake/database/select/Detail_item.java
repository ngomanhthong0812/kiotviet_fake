package com.example.kiotviet_fake.database.select;

import com.example.kiotviet_fake.models.DetailBill;

import retrofit2.Call;
import retrofit2.http.GET;
import java.util.List;

public interface Detail_item {
    @GET("bill_items/detail_bill.php")
    Call<String> getDetail_item();
}
