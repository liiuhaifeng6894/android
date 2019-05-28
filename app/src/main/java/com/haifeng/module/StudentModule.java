package com.haifeng.module;

import com.haifeng.bean.Student;
import com.haifeng.bean.Students;

import dagger.Module;
import dagger.Provides;

@Module
public class StudentModule {

    @Provides
    public String provideName() {
        return "lisi";
    }

    @Provides
    public int provideScore() {
        return 10;
    }

    @Provides
    public boolean provideAge() {
        return true;
    }

    @Provides
    public Students provideStudent(String name, int score, boolean age) {
        return new Students(name, score, age);
    }

}
