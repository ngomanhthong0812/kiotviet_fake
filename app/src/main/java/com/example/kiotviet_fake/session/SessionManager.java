package com.example.kiotviet_fake.session;

import com.example.kiotviet_fake.models.Order;

import java.util.ArrayList;

public class SessionManager {
    private static SessionManager instance;
    private ArrayList<Order> orders;

    private SessionManager() {
        orders = new ArrayList<>();
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
    public void removeOrderByProductId(int productId) {
        for (Order order : orders) {
            if (order.getProductId() == productId) {
                orders.remove(order);
                break;
            }
        }
    }
    public void updateQuantityProduct(int productId, int newQuantity) {
        for (Order order : orders) {
            if (order.getProductId() == productId) {
                order.setQuantity(newQuantity);
                break;
            }
        }
    }

    public void removeAll(){
        orders.clear();
    }

}