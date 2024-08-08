package com.sajan.universityattendanceapp;

import android.os.Bundle;
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

public class StudentDashboardActivity extends AppCompatActivity {

    private RecyclerView courseRecyclerView;
    private CourseAdapter courseAdapter;
    private List<String> courseList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        courseRecyclerView = findViewById(R.id.courseRecyclerView);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        courseList = new ArrayList<>();
        courseList.add("Math 101");
        courseList.add("Science 202");
        courseList.add("Physics 203");
        courseList.add("Chemistry 204");
        courseList.add("Mechanics 205");
        courseList.add("History 206");
        courseList.add("Political Science 207");
        courseList.add("economics 208");
        courseList.add("Geography 209");


        courseAdapter = new CourseAdapter(courseList,this);
        courseRecyclerView.setAdapter(courseAdapter);

        loadCourses();
    }

    private void loadCourses() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("courses").orderByChild("students/" + userId).equalTo(true)
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
                        Toast.makeText(StudentDashboardActivity.this, "Failed to load courses", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void markAttendance(String course) {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference attendanceRef = mDatabase.child("attendance").child(course).child(userId);

        // Set the attendance status to "present"
        attendanceRef.setValue("present").addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(StudentDashboardActivity.this, "Attendance marked for " + course, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StudentDashboardActivity.this, "Failed to mark attendance", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
