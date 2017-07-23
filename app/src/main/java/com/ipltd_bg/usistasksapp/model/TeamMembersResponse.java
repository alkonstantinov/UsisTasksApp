package com.ipltd_bg.usistasksapp.model;

import java.util.ArrayList;

/**
 * Created by avlaev on 9/3/15.
 */
public class TeamMembersResponse {

    private ArrayList<LoginResponse.LoginData> users = new ArrayList<>();

    public ArrayList<LoginResponse.LoginData> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<LoginResponse.LoginData> users) {
        this.users = users;
    }
}
