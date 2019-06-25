package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "imageurls", schema = "picturetagdatabase")
public class Imageurls {
    private int id;
    private String taskId;
    private String imageUrl;

    public Imageurls(String taskId, String imageUrl) {
        this.taskId = taskId;
        this.imageUrl = imageUrl;
    }

    public Imageurls() {
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "imageURL", nullable = true, length = 80)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Imageurls imageurls = (Imageurls) o;
        return id == imageurls.id &&
                Objects.equals(taskId, imageurls.taskId) &&
                Objects.equals(imageUrl, imageurls.imageUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, taskId, imageUrl);
    }
}
