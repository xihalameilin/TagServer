package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "taskstatusrecord", schema = "picturetagdatabase")
public class Taskstatusrecord {
    private int id;
    private String userId;
    private String taskId;
    private Integer status;     //0：未提交 1：已提交 2：已评审
    private Double points;
    private Timestamp obtainedTime;
    private Timestamp commitTime;

    public Taskstatusrecord(String userId, String taskId, Integer status, Double points, Timestamp obtainedTime, Timestamp commitTime) {
        this.userId = userId;
        this.taskId = taskId;
        this.status = status;
        this.points = points;
        this.obtainedTime = obtainedTime;
        this.commitTime = commitTime;
    }

    public Taskstatusrecord() {
    }

    @Id
    @Column(name = "ID", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "userID", nullable = true, length = 20)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "taskID", nullable = true, length = 50)
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "Status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "points", nullable = true, precision = 0)
    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    @Basic
    @Column(name = "obtainedTime", nullable = true)
    public Timestamp getObtainedTime() {
        return obtainedTime;
    }

    public void setObtainedTime(Timestamp obtainedTime) {
        this.obtainedTime = obtainedTime;
    }

    @Basic
    @Column(name = "commitTime", nullable = true)
    public Timestamp getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Timestamp commitTime) {
        this.commitTime = commitTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Taskstatusrecord that = (Taskstatusrecord) o;
        return id == that.id &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(taskId, that.taskId) &&
                Objects.equals(status, that.status) &&
                Objects.equals(points, that.points) &&
                Objects.equals(obtainedTime, that.obtainedTime) &&
                Objects.equals(commitTime, that.commitTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, userId, taskId, status, points, obtainedTime, commitTime);
    }
}
