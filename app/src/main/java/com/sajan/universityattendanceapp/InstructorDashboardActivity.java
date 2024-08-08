package com.sajan.universityattendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class InstructorDashboardActivity extends AppCompatActivity {

    private RecyclerView courseRecyclerView;
    private CourseAdapter courseAdapter;
    private List<String> courseList;
    private Button addCourseButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_dashboard);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        courseRecyclerView = findViewById(R.id.courseRecyclerView);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        courseList = new ArrayList<>();
        courseAdapter = new CourseAdapter(courseList);
        courseRecyclerView.setAdapter(courseAdapter);

        addCourseButton = findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(v -> startActivity(new Intent(this, AddCourseActivity.class)));

        loadCourses();
    }

    private void loadCourses() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("courses").orderByChild("instructorId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                            String courseName = courseSnapshot.child("courseName").getValue(String.class);
                            courseList.add(courseName);
                        }
                        courseAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(InstructorDashboardActivity.this, "Failed to load courses", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
