package com.mss.video.stream.service;

import com.mss.video.stream.entities.Video;
import com.mss.video.stream.repositories.VideoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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

    @Value("${files.video_hls}")
    private String HLS_DIRECTORY;

    @PostConstruct
    public void init() {
        File file = new File(DIRECTORY);

        if (!file.exists()) {
            file.mkdir();
            System.out.println("Folder created");
        } else {
            System.out.println("Folder already exists");
        }
        try {
            Files.createDirectories(Paths.get(HLS_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException(e);
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

            // save video in db
            Video savedVideo = this.videoRepository.save(video);

            // file process
            processVideo(savedVideo.getVideoId());

            return video;

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

    public String processVideo(String videoId) {

        Video video = this.findVideoById(videoId);
        String filePath = video.getFilePath();

        //path where to store data:
        Path videoPath = Paths.get(filePath);


//        String output360p = HSL_DIR + videoId + "/360p/";
//        String output720p = HSL_DIR + videoId + "/720p/";
//        String output1080p = HSL_DIR + videoId + "/1080p/";

        try {
//            Files.createDirectories(Paths.get(output360p));
//            Files.createDirectories(Paths.get(output720p));
//            Files.createDirectories(Paths.get(output1080p));

            // ffmpeg command
            Path outputPath = Paths.get(HLS_DIRECTORY, videoId);

            Files.createDirectories(outputPath);


            String ffmpegCmd = String.format(
                    "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%3d.ts\"  \"%s/master.m3u8\" ",
                    videoPath, outputPath, outputPath
            );

//            StringBuilder ffmpegCmd = new StringBuilder();
//            ffmpegCmd.append("ffmpeg  -i ")
//                    .append(videoPath.toString())
//                    .append(" -c:v libx264 -c:a aac")
//                    .append(" ")
//                    .append("-map 0:v -map 0:a -s:v:0 640x360 -b:v:0 800k ")
//                    .append("-map 0:v -map 0:a -s:v:1 1280x720 -b:v:1 2800k ")
//                    .append("-map 0:v -map 0:a -s:v:2 1920x1080 -b:v:2 5000k ")
//                    .append("-var_stream_map \"v:0,a:0 v:1,a:0 v:2,a:0\" ")
//                    .append("-master_pl_name ").append(HSL_DIR).append(videoId).append("/master.m3u8 ")
//                    .append("-f hls -hls_time 10 -hls_list_size 0 ")
//                    .append("-hls_segment_filename \"").append(HSL_DIR).append(videoId).append("/v%v/fileSequence%d.ts\" ")
//                    .append("\"").append(HSL_DIR).append(videoId).append("/v%v/prog_index.m3u8\"");


            System.out.println(ffmpegCmd);
            //file this command
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", ffmpegCmd);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            int exit = process.waitFor();
            if (exit != 0) {
                throw new RuntimeException("video processing failed!!");
            }

            return videoId;


        } catch (IOException ex) {
            throw new RuntimeException("Video processing fail!!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}
