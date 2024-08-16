package com.mss.video.stream.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "yt_videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {

    @Id
    private String videoId;

    private String title;

    private String description;

    private String contentType;

    private String filePath;

//    @ManyToOne
//    @JoinColumn(name = "course_id_fk", referencedColumnName = "courseId")
//    private Course course;

}
