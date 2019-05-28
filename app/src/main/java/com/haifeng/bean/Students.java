package com.haifeng.bean;


public class Students {
    private String name;
    private int score;
    private boolean age;

    @Override
    public String toString() {
        return "Students{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", age=" + age +
                '}';
    }

    public Students(String name, int score, boolean age) {
        this.name = name;
        this.score = score;
        this.age = age;
    }


}
