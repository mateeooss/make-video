package com.makevideo.make_video;

import org.bytedeco.ffmpeg.global.avformat;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoWriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.bytedeco.ffmpeg.global.avutil;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class MakeVideoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MakeVideoApplication.class, args);
	}

}
