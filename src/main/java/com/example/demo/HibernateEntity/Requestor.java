package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "requestors", schema = "picturetagdatabase")
public class Requestor extends User{
    private String userId;
    private String password;
    private int userType;
    private String avatarUrl;
    private String email;

    public Requestor(String userId, String password, String avatarUrl, String email) {
        this.userId = userId;
        this.password = password;
        this.userType = 2;
        this.avatarUrl = avatarUrl;
        this.email = email;
    }

    public Requestor() {
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
    @Column(name = "password", nullable = true, length = 20)
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

    @Basic
    @Column(name = "email", nullable = true, length = 30)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Requestor that = (Requestor) o;
        return userType == that.userType &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(password, that.password) &&
                Objects.equals(avatarUrl, that.avatarUrl) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, password, userType, avatarUrl, email);
    }
}
