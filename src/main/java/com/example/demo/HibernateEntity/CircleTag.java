package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "circletags", schema = "picturetagdatabase")
public class CircleTag {
    private int circleTagId;
    private int circleTagSetId;
    private String label;
    private String description;
    private String x;
    private String y;
    private String drawColor;
    private String drawSize;
    private String fontSize;
    private String fontFamily;
    private String tagOption;

    public CircleTag(int circleTagSetId, String label, String description, String x, String y, String drawColor, String drawSize, String fontSize, String fontFamily, String tagOption) {
        this.circleTagSetId = circleTagSetId;
        this.label = label;
        this.description = description;
        this.x = x;
        this.y = y;
        this.drawColor = drawColor;
        this.drawSize = drawSize;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
        this.tagOption = tagOption;
    }

    public CircleTag() {
    }

    @Id
    @Column(name = "CircleTagID", nullable = false)
    public int getCircleTagId() {
        return circleTagId;
    }

    public void setCircleTagId(int circleTagId) {
        this.circleTagId = circleTagId;
    }

    @Basic
    @Column(name = "CircleTagSetID", nullable = true)
    public int getCircleTagSetId() {
        return circleTagSetId;
    }

    public void setCircleTagSetId(int circleTagSetId) {
        this.circleTagSetId = circleTagSetId;
    }

    @Basic
    @Column(name = "label", nullable = true, length = 30)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "x", nullable = true, length = 3000)
    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    @Basic
    @Column(name = "y", nullable = true, length = 3000)
    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    @Basic
    @Column(name = "drawColor", nullable = true, length = 30)
    public String getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(String drawColor) {
        this.drawColor = drawColor;
    }

    @Basic
    @Column(name = "drawSize", nullable = true, length = 30)
    public String getDrawSize() {
        return drawSize;
    }

    public void setDrawSize(String drawSize) {
        this.drawSize = drawSize;
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
        CircleTag that = (CircleTag) o;
        return circleTagId == that.circleTagId &&
                Objects.equals(circleTagSetId, that.circleTagSetId) &&
                Objects.equals(label, that.label) &&
                Objects.equals(description, that.description) &&
                Objects.equals(x, that.x) &&
                Objects.equals(y, that.y) &&
                Objects.equals(drawColor, that.drawColor) &&
                Objects.equals(drawSize, that.drawSize) &&
                Objects.equals(fontSize, that.fontSize) &&
                Objects.equals(fontFamily, that.fontFamily) &&
                Objects.equals(tagOption, that.tagOption);
    }

    @Override
    public int hashCode() {

        return Objects.hash(circleTagId, circleTagSetId, label, description, x, y, drawColor, drawSize, fontSize, fontFamily, tagOption);
    }
}
