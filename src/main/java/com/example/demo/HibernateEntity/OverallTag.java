package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "overalltag", schema = "picturetagdatabase")
public class OverallTag {
    private int overallTagId;
    private String url;
    private String userId;
    private String taskId;
    private String overallTitle;
    private String overallDescription;
    private String fontSize;
    private String fontFamily;

    public OverallTag(String url, String userId, String taskId, String overallTitle, String overallDescription, String fontSize, String fontFamily) {
        this.url = url;
        this.userId = userId;
        this.taskId = taskId;
        this.overallTitle = overallTitle;
        this.overallDescription = overallDescription;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
    }

    public OverallTag() {
    }

    @Id
    @Column(name = "OverallTagID", nullable = false)
    public int getOverallTagId() {
        return overallTagId;
    }

    public void setOverallTagId(int overallTagId) {
        this.overallTagId = overallTagId;
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

    @Basic
    @Column(name = "overallTitle", nullable = true, length = 80)
    public String getOverallTitle() {
        return overallTitle;
    }

    public void setOverallTitle(String overallTitle) {
        this.overallTitle = overallTitle;
    }

    @Basic
    @Column(name = "overallDescription", nullable = true, length = 200)
    public String getOverallDescription() {
        return overallDescription;
    }

    public void setOverallDescription(String overallDescription) {
        this.overallDescription = overallDescription;
    }

    @Basic
    @Column(name = "fontSize", nullable = true, length = 30)
    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    @Basic
    @Column(name = "fontFamily", nullable = true, length = 30)
    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OverallTag that = (OverallTag) o;
        return Objects.equals(overallTagId, that.overallTagId) &&
                Objects.equals(url, that.url) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(taskId, that.taskId) &&
                Objects.equals(overallTitle, that.overallTitle) &&
                Objects.equals(overallDescription, that.overallDescription) &&
                Objects.equals(fontSize, that.fontSize) &&
                Objects.equals(fontFamily, that.fontFamily);
    }

    @Override
    public int hashCode() {

        return Objects.hash(overallTagId, url, userId, taskId, overallTitle, overallDescription, fontSize, fontFamily);
    }

    @Override
    public String toString() {
        return "OverallTag{" +
                "overallTagId=" + overallTagId +
                ", url='" + url + '\'' +
                ", userId='" + userId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", overallTitle='" + overallTitle + '\'' +
                ", overallDescription='" + overallDescription + '\'' +
                ", fontSize='" + fontSize + '\'' +
                ", fontFamily='" + fontFamily + '\'' +
                '}';
    }
}
