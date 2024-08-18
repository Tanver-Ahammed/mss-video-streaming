package com.mss.video.stream.controller;

import com.mss.video.stream.entities.Video;
import com.mss.video.stream.payload.CustomMessage;
import com.mss.video.stream.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/videos")
@CrossOrigin("http://localhost:5173/")
public class VideoController {

    @Autowired
    private VideoService videoService;

    // video uploader
    @PostMapping
    public ResponseEntity<?> createVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description
    ) {
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoId(UUID.randomUUID().toString());
        Video savedVideo = videoService.save(video, file);

        if (savedVideo != null) {
            return ResponseEntity.status(HttpStatus.OK).body(video);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomMessage.builder().message("Video not uploaded ")
                            .success(false).build());
        }
    }

    // stream video
    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> stream(
            @PathVariable("videoId") String videoId
    ) {

        Video video = this.videoService.findById(videoId);
        String contentType = video.getContentType();
        String filePath = video.getFilePath();

        return null;
    }

}
