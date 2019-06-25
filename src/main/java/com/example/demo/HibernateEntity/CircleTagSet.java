package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "circletagsets", schema = "picturetagdatabase")
public class CircleTagSet {
    private int circleTagSetId;
    private String url;
    private String userId;
    private String taskId;
    private Set<CircleTag> circleTags;

    public CircleTagSet(String url, String userId, String taskId, Set<CircleTag> circleTags) {
        this.url = url;
        this.userId = userId;
        this.taskId = taskId;
        this.circleTags = circleTags;
    }

    public CircleTagSet() {
    }

    @Id
    @Column(name = "circleTagSetID", nullable = false)
    public int getCircleTagSetId() {
        return circleTagSetId;
    }

    private void setCircleTagSetId(int circleTagSetId) {
        this.circleTagSetId = circleTagSetId;
    }

    @Basic
    @Column(name = "url", nullable = true, length = 50)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
    @Column(name = "taskID", nullable = true, length = 20)
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CircleTagSet that = (CircleTagSet) o;
        return Objects.equals(circleTagSetId, that.circleTagSetId) &&
                Objects.equals(url, that.url) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(circleTagSetId, url, userId, taskId);
    }

    @OneToMany
    public Set<CircleTag> getCircleTags() {
        return circleTags;
    }

    public void setCircleTags(Set<CircleTag> circleTags) {
        this.circleTags = circleTags;
    }
}
