package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "managers", schema = "picturetagdatabase")
public class Manager extends User{
    private String userId;
    private String password;
    private int userType;
    private String avatarUrl;

    public Manager(String userId, String password, String avatarUrl) {
        this.userId = userId;
        this.password = password;
        this.userType = 3;
        this.avatarUrl = avatarUrl;
    }

    public Manager() {
    }

    @Id
    @Column(name = "userID", nullable = false, length = 20)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 20)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "userType", nullable = false)
    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Basic
    @Column(name = "avatarUrl", nullable = true, length = 100)
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return userType == manager.userType &&
                Objects.equals(userId, manager.userId) &&
                Objects.equals(password, manager.password) &&
                Objects.equals(avatarUrl, manager.avatarUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, password, userType, avatarUrl);
    }
}
