package com.mss.video.stream.repositories;

import com.mss.video.stream.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, String> {

    Optional<Video> findByTitle(String title);

}
