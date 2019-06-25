package com.example.demo.HibernateEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "awards_ranking", schema = "picturetagdatabase")
public class Awards_Ranking {
    private String userId;
    private double points;
    private int ranking;

    @Id
    @Column(name = "userID", nullable = false, length = 20)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
    @Column(name = "ranking", nullable = false)
    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Awards_Ranking that = (Awards_Ranking) o;
        return Double.compare(that.points, points) == 0 &&
                ranking == that.ranking &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, points, ranking);
    }
}
