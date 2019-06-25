package com.example.demo.serviceImpl.AI;

import java.io.File;
import java.io.IOException;

public class filetest {

    public static void main(String args[]){
        File file=new File("src\\main\\resources\\META-INF\\resources\\1.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
