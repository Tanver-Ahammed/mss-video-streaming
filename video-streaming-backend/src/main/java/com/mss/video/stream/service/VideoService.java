package com.mss.video.stream.service;

import com.mss.video.stream.entities.Video;
import com.mss.video.stream.repositories.VideoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Value("${files.video}")
    private String DIRECTORY;

    @PostConstruct
    public void init() {
        File file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
            System.out.println("Folder created");
        } else {
            System.out.println("Folder already exists");
        }
    }

    public Video saveVideo(Video video, MultipartFile file) {

        try {
            // original file name
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();

            // folder path create
            String cleanFileName = StringUtils.cleanPath(fileName);
            String cleanFolder = StringUtils.cleanPath(DIRECTORY);

            Path path = Paths.get(cleanFolder, cleanFileName);

            // copy file
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            // set video meta data
            video.setContentType(contentType);
            video.setFilePath(path.toString());

            return this.videoRepository.save(video);

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Video findVideoById(String videoId) {
        return this.videoRepository.findById(videoId).orElseThrow(
                () -> new RuntimeException("Video not found")
        );
    }

    public Video findVideoByTitle(String title) {
        return null;
    }

    public List<Video> findAllVideos() {
        return this.videoRepository.findAll();
    }

}
