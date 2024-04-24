package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SelectTablesService {
    @FormUrlEncoded
    @POST("tables/selectTables.php")
    Call<String> getTable(
            @Field("user_id") int user_id,
            @Field("table_name") String table_name
    );
}
