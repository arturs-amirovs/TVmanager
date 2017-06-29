package com.example.android.tvmanager;

/**
 * Created by arturs.amirovs on 26/06/2017.
 */

class User {
    private static User currentUser = null;

    private String uid = "";

    private User() {
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            currentUser = new User();
        }
        return currentUser;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
