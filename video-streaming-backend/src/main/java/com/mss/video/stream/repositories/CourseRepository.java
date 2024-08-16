package com.mss.video.stream.repositories;

import com.mss.video.stream.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, String> {
}
