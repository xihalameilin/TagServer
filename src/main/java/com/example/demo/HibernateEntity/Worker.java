package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "workers", schema = "picturetagdatabase")
public class Worker extends User {
    private String userId;
    private String password;
    private int userType;
    private double points;
    private String avatarUrl;
    private String email;

    public Worker() {
    }

    public Worker(String userId, String password, double points, String avatarUrl, String email) {
        this.userId = userId;
        this.password = password;
        this.userType=1;
        this.points = points;
        this.avatarUrl = avatarUrl;
        this.email = email;
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
    @Column(name = "points", nullable = false, precision = 0)
    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
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
        Worker workers = (Worker) o;
        return userType == workers.userType &&
                Double.compare(workers.points, points) == 0 &&
                Objects.equals(userId, workers.userId) &&
                Objects.equals(password, workers.password) &&
                Objects.equals(avatarUrl, workers.avatarUrl) &&
                Objects.equals(email, workers.email);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, password, userType, points, avatarUrl, email);
    }
}
