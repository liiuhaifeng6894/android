package com.haifeng.bean;

import javax.inject.Inject;

public class Student {
    private String name;
    private int score;
    public String getName() {
        return name;
    }
    @Inject
    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return "Student{" + "name='" + name + '\'' + ", score=" + score + '}';
    }

}
