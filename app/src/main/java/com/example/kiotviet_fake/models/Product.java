package com.example.kiotviet_fake.models;

public class Product {
    public int id;
    public String idProductItem;
    public String name;
    public String price;
    public int quantity;
    public int quantityOrder;
    public int idTable;
    public String nameTable;

    public Product(int id, String idProductItem, String name, String price, int quantity, int quantityOrder, int idTable, String nameTable) {
        this.id = id;
        this.idProductItem = idProductItem;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.quantityOrder = quantityOrder;
        this.idTable = idTable;
        this.nameTable = nameTable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdProductItem() {
        return idProductItem;
    }

    public void setIdProductItem(String idProductItem) {
        this.idProductItem = idProductItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantityOrder() {
        return quantityOrder;
    }

    public void setQuantityOrder(int quantityOrder) {
        this.quantityOrder = quantityOrder;
    }

    public int getIdTable() {
        return idTable;
    }

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }
}