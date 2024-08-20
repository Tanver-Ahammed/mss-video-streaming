package com.mss.video.stream;

import com.mss.video.stream.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VideoStreamingBackendApplicationTests {

    @Autowired
    private VideoService videoService;

    @Test
    void contextLoads() {

        this.videoService.processVideo("692d568c-c579-44a4-80e0-e586da7735be");

    }

}
