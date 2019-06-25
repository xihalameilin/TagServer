package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "task", schema = "picturetagdatabase")
public class Task {
    private String taskId;
    private String requestorId;
    private Timestamp beginTime;
    private Timestamp endTime;
    private String taskName;
    private String taskDescription;
    private Integer workingNum;
    private Integer finishNum;
    private Double points;
    private Double workerLevel;
    private String labels;
    private Set<Imageurls> imageUrls;
    private Set<Options> options;

    public Task(String taskId, String requestorId, Timestamp beginTime, Timestamp endTime, String taskName, String taskDescription, Double points, Double workerLevel, String labels, ArrayList<String> imageURLs, ArrayList<String> options) {
        this.taskId = taskId;
        this.requestorId = requestorId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.points = points;
        this.workerLevel = workerLevel;
        this.labels = labels;
        this.workingNum = 0;
        this.finishNum = 0;
        Set set = new HashSet();
        for (String str : imageURLs) {
            set.add(new Imageurls(taskId, str));
        }
        Set set1 = new HashSet();
        for (String str : options) {
            set1.add(new Options(taskId, str));
        }
        this.imageUrls = set;
        this.options = set1;
    }

    public Task() {
    }

    @Id
    @Column(name = "taskID", nullable = false, length = 20)
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "requestorID", nullable = true, length = 20)
    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    @Basic
    @Column(name = "beginTime", nullable = true)
    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    @Basic
    @Column(name = "endTime", nullable = true)
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "taskName", nullable = true, length = 20)
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Basic
    @Column(name = "taskDescription", nullable = true, length = 50)
    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @Basic
    @Column(name = "workingNum", nullable = true)
    public Integer getWorkingNum() {
        return workingNum;
    }

    public void setWorkingNum(Integer workingNum) {
        this.workingNum = workingNum;
    }

    @Basic
    @Column(name = "finishNum", nullable = true)
    public Integer getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(Integer finishNum) {
        this.finishNum = finishNum;
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
    @Column(name = "workerLevel", nullable = true, precision = 0)
    public Double getWorkerLevel() {
        return workerLevel;
    }

    public void setWorkerLevel(Double workerLevel) {
        this.workerLevel = workerLevel;
    }

    @Basic
    @Column(name = "labels", nullable = true, length = 30)
    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId) &&
                Objects.equals(requestorId, task.requestorId) &&
                Objects.equals(beginTime, task.beginTime) &&
                Objects.equals(endTime, task.endTime) &&
                Objects.equals(taskName, task.taskName) &&
                Objects.equals(taskDescription, task.taskDescription) &&
                Objects.equals(workingNum, task.workingNum) &&
                Objects.equals(finishNum, task.finishNum) &&
                Objects.equals(points, task.points) &&
                Objects.equals(workerLevel, task.workerLevel) &&
                Objects.equals(labels, task.labels);
    }

    @Override
    public int hashCode() {

        return Objects.hash(taskId, requestorId, beginTime, endTime, taskName, taskDescription, workingNum, finishNum, points, workerLevel, labels);
    }

    @OneToMany
    public Set<Imageurls> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Set<Imageurls> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @OneToMany
    public Set<Options> getOptions() {
        return options;
    }

    public void setOptions(Set<Options> options) {
        this.options = options;
    }
}
