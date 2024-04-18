package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface BillItemsSelectByUserIdService {
    @FormUrlEncoded
    @POST("bill_items/select_detail_by_id_user.php")
    Call<String> getBillItems(
            @Field("user_id") int user_id
    );
}


