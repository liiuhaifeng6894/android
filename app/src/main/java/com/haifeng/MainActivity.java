package com.haifeng;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.haifeng.bean.Students;
import com.haifeng.component.DaggerMainActivityComponent;
import com.haifeng.bean.Student;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    public Students students;
    @Inject
    public Student student;
    public final String TAG = getClass().getSimpleName();
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerMainActivityComponent.builder().build().inject(this);
//        Toast.makeText(this, students.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG, student.toString());
        Log.e(TAG, students.toString());

    }
}
