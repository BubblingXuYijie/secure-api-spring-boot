package icu.xuyijie.entity;

import icu.xuyijie.secureapi.annotation.DecryptParam;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author 徐一杰
 * @date 2024/6/25 11:03
 * @description
 */
public class User extends People {
    private int id;

    @DecryptParam
    private List<String> role;

    @DecryptParam
    private LocalDateTime createTime;

    @DecryptParam
    private Date editTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", role=" + role +
                ", createTime=" + createTime +
                ", editTime=" + editTime +
                '}';
    }
}
