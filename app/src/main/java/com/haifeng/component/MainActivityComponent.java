package com.haifeng.component;

import com.haifeng.MainActivity;
import com.haifeng.module.StudentModule;

import dagger.Component;

@Component(modules = StudentModule.class)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
