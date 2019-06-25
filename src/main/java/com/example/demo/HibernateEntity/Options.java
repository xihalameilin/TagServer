package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "options", schema = "picturetagdatabase")
public class Options {
    private int optionId;
    private String taskId;
    private String tagOption;

    public Options(String taskId, String tagOption) {
        this.taskId = taskId;
        this.tagOption = tagOption;
    }

    public Options() {

    }
    @Id
    @Column(name = "optionId", nullable = false)
    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
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
    @Column(name = "tagOption", nullable = true, length = 15)
    public String getTagOption() {
        return tagOption;
    }

    public void setTagOption(String option) {
        this.tagOption = option;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Options options = (Options) o;
        return optionId == options.optionId &&
                Objects.equals(taskId, options.taskId) &&
                Objects.equals(tagOption, options.tagOption);
    }

    @Override
    public int hashCode() {

        return Objects.hash(optionId, taskId, tagOption);
    }
}
