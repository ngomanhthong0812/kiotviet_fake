package com.example.kiotviet_fake.interface_main;

public interface AdapterListener {
    // Implement phương thức từ Interface
    void onItemDeleted();

    void finishActivity();

    void update_totalQuantity_totalPrice(int quantity, float priceTotal);

    void notification_insertOrder(int idTable, String nameTable);
}
