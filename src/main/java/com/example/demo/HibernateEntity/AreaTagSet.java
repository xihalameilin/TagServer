package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "areatagsets", schema = "picturetagdatabase")
public class AreaTagSet {
    private int areaTagSetId;
    private String url;
    private String userId;
    private String taskId;
    private Set<AreaTag> areaTags;

    public AreaTagSet() {
    }

    public AreaTagSet(String url, String userId, String taskId, Set areaTags) {
        this.url = url;
        this.userId = userId;
        this.taskId = taskId;
        this.areaTags = areaTags;
    }

    @Id
    @Column(name = "areaTagSetID", nullable = false)
    public int getAreaTagSetId() {
        return areaTagSetId;
    }

    public void setAreaTagSetId(int areaTagSetId) {
        this.areaTagSetId = areaTagSetId;
    }

    @Basic
    @Column(name = "url", nullable = false, length = 50)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "userID", nullable = false, length = 20)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "taskID", nullable = false, length = 20)
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
        AreaTagSet that = (AreaTagSet) o;
        return Objects.equals(areaTagSetId, that.areaTagSetId) &&
                Objects.equals(url, that.url) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(areaTagSetId, url, userId, taskId);
    }

    @OneToMany
    public Set<AreaTag> getAreaTags() {
        return areaTags;
    }

    public void setAreaTags(Set<AreaTag> areaTags) {
        this.areaTags = areaTags;
    }
}
