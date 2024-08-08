package com.sajan.universityattendanceapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<String> courseList;
    private StudentDashboardActivity studentDashboardActivity;

    public CourseAdapter(List<String> courseList, StudentDashboardActivity studentDashboardActivity) {
        this.courseList = courseList;
        this.studentDashboardActivity = studentDashboardActivity;
    }

    public CourseAdapter(List<String> courseList) {
        this.courseList = courseList;
    }


    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        String course = courseList.get(position);
        holder.courseNameTextView.setText(course);

        // Set click listener to mark attendance
        holder.itemView.setOnClickListener(v -> studentDashboardActivity.markAttendance(course));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView courseNameTextView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.courseNameTextView);
        }
    }
}
