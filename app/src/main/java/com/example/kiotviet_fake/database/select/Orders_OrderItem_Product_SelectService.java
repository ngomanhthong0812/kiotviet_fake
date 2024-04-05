package com.example.kiotviet_fake.database.select;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Orders_OrderItem_Product_SelectService {
    @GET("orders/selectfrom_order_orderitem_product.php")
    Call<String> getOrders();
}
