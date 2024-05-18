package com.example.kiotviet_fake.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SessionCategories {

    private static final String PREF_NAME = "session_categories";
    private static final String KEY_CATEGORIES = "categories";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public SessionCategories(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    // Thêm tên loại vào danh sách
    public void addCategory(String category) {
        Set<String> categories = getCategoriesSet();
        categories.add(category);
        saveCategories(categories);
    }

    // Xóa tất cả các tên loại
    public void clearCategories() {
        editor.remove(KEY_CATEGORIES).apply();
    }

    // Lấy danh sách tất cả các tên loại
    public List<String> getCategories() {
        String categoriesJson = sharedPreferences.getString(KEY_CATEGORIES, null);
        if (categoriesJson != null) {
            Type type = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(categoriesJson, type);
        } else {
            return new ArrayList<>();
        }
    }

    // Lấy danh sách tất cả các tên loại dưới dạng Set (sử dụng cho việc thêm mới)
    private Set<String> getCategoriesSet() {
        String categoriesJson = sharedPreferences.getString(KEY_CATEGORIES, null);
        if (categoriesJson != null) {
            Type type = new TypeToken<Set<String>>() {}.getType();
            return gson.fromJson(categoriesJson, type);
        } else {
            return new HashSet<>();
        }
    }

    // Lưu danh sách tên loại
    private void saveCategories(Set<String> categories) {
        String categoriesJson = gson.toJson(categories);
        editor.putString(KEY_CATEGORIES, categoriesJson).apply();
    }
}
