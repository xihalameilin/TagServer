package com.example.demo.daoImpl;

import com.example.demo.HibernateEntity.AreaTag;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class AreaTagDAOImplTest {

    @Test
    public void add() {
    }

    @Test
    public void modify() {
    }

    @Test
    public void modify1() {
    }

    @Test
    public void getSet() {
    }

    @Test
    public void getTagByOption() {
        Set<AreaTag> areaTags = new AreaTagDAOImpl().getTagByOption("lml", "gsy180622214826", "http://localhost:9900/gsy180622214826_2.jpeg", "脖子");
        for (AreaTag areaTag : areaTags) {
            System.out.println(areaTag.toString());
        }
    }

    @Test
    public void getTagById() {
    }

    @Test
    public void delete() {
    }
}