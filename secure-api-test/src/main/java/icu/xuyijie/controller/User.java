package icu.xuyijie.controller;

import icu.xuyijie.secureapi.annotation.DecryptParam;

import java.util.List;

/**
 * @author 徐一杰
 * @date 2024/6/25 11:03
 * @description
 */
public class User {
    private int id;
    private String name;
    @DecryptParam
    private List<String> role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", role=" + role +
                '}';
    }
}
