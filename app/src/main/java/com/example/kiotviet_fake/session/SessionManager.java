package com.example.kiotviet_fake.session;

public class SessionManager {
    private static SessionManager instance;
    private int count = 0;

    private SessionManager() {
        // Không cho phép tạo đối tượng từ bên ngoài lớp
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}