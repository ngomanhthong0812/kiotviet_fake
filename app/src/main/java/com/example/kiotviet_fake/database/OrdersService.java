package com.example.kiotviet_fake.database;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OrdersService {
    @GET("orders/selectfrom_order_orderitem_product.php")
    Call<String> getOrders();
}
