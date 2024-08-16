package com.mss.video.stream.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "yt_courses")
public class Course {

    @Id
    private String courseId;

    private String courseName;

    private String description;

//    @OneToMany(mappedBy = "course", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Video> videos = new ArrayList<>();

}
