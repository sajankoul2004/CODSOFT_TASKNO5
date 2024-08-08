package com.sajan.universityattendanceapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCourseActivity extends AppCompatActivity {

    private EditText courseNameEditText;
    private Button saveCourseButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        courseNameEditText = findViewById(R.id.courseNameEditText);
        saveCourseButton = findViewById(R.id.saveCourseButton);

        saveCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCourse();
            }
        });
    }

    private void saveCourse() {
        String courseName = courseNameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(courseName)) {
            Toast.makeText(AddCourseActivity.this, "Course name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        String courseId = mDatabase.child("courses").push().getKey();
        String userId = mAuth.getCurrentUser().getUid();
        Course course = new Course(courseId, courseName, userId);
        mDatabase.child("courses").child(courseId).setValue(course).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddCourseActivity.this, "Course added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddCourseActivity.this, "Failed to add course", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class Course {
        public String courseId;
        public String courseName;
        public String instructorId;

        public Course(String courseId, String courseName, String instructorId) {
            this.courseId = courseId;
            this.courseName = courseName;
            this.instructorId = instructorId;
        }
    }
}
