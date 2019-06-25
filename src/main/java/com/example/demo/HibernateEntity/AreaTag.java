package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "areatags", schema = "picturetagdatabase")
public class AreaTag {
    private int areaTagId;
    private int areaTagSetId;
    private String label;
    private String description;
    private String beginX;
    private String beginY;
    private String endX;
    private String endY;
    private String drawColor;
    private String drawSize;
    private String fontSize;
    private String fontFamily;
    private String tagOption;

    public AreaTag() {
    }

    public AreaTag(int areaTagSetId, String label, String description, String beginX, String beginY, String endX, String endY, String drawColor, String drawSize, String fontSize, String fontFamily, String tagOption) {
        this.areaTagSetId = areaTagSetId;
        this.label = label;
        this.description = description;
        this.beginX = beginX;
        this.beginY = beginY;
        this.endX = endX;
        this.endY = endY;
        this.drawColor = drawColor;
        this.drawSize = drawSize;
        this.fontSize = fontSize;
        this.fontFamily = fontFamily;
        this.tagOption = tagOption;
    }


    @Id
    @Column(name = "areaTagID", nullable = false)
    public int getAreaTagId() {
        return areaTagId;
    }

    public void setAreaTagId(int areaTagId) {
        this.areaTagId = areaTagId;
    }

    @Basic
    @Column(name = "areaTagSetID", nullable = true)
    public int getAreaTagSetId() {
        return areaTagSetId;
    }

    public void setAreaTagSetId(int areaTagSetId) {
        this.areaTagSetId = areaTagSetId;
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
    @Column(name = "beginX", nullable = true, length = 3000)
    public String getBeginX() {
        return beginX;
    }

    public void setBeginX(String beginX) {
        this.beginX = beginX;
    }

    @Basic
    @Column(name = "beginY", nullable = true, length = 3000)
    public String getBeginY() {
        return beginY;
    }

    public void setBeginY(String beginY) {
        this.beginY = beginY;
    }

    @Basic
    @Column(name = "endX", nullable = true, length = 3000)
    public String getEndX() {
        return endX;
    }

    public void setEndX(String endX) {
        this.endX = endX;
    }

    @Basic
    @Column(name = "endY", nullable = true, length = 3000)
    public String getEndY() {
        return endY;
    }

    public void setEndY(String endY) {
        this.endY = endY;
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
    public String toString() {
        return "AreaTag{" +
                "areaTagId=" + areaTagId +
                ", areaTagSetId='" + areaTagSetId + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", beginX='" + beginX + '\'' +
                ", beginY='" + beginY + '\'' +
                ", endX='" + endX + '\'' +
                ", endY='" + endY + '\'' +
                ", drawColor='" + drawColor + '\'' +
                ", drawSize='" + drawSize + '\'' +
                ", fontSize='" + fontSize + '\'' +
                ", fontFamily='" + fontFamily + '\'' +
                ", option='" + tagOption + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AreaTag that = (AreaTag) o;
        return areaTagId == that.areaTagId &&
                Objects.equals(areaTagSetId, that.areaTagSetId) &&
                Objects.equals(label, that.label) &&
                Objects.equals(description, that.description) &&
                Objects.equals(beginX, that.beginX) &&
                Objects.equals(beginY, that.beginY) &&
                Objects.equals(endX, that.endX) &&
                Objects.equals(endY, that.endY) &&
                Objects.equals(drawColor, that.drawColor) &&
                Objects.equals(drawSize, that.drawSize) &&
                Objects.equals(fontSize, that.fontSize) &&
                Objects.equals(fontFamily, that.fontFamily) &&
                Objects.equals(tagOption, that.tagOption);
    }

    @Override
    public int hashCode() {

        return Objects.hash(areaTagId, areaTagSetId, label, description, beginX, beginY, endX, endY, drawColor, drawSize, fontSize, fontFamily, tagOption);
    }
}
