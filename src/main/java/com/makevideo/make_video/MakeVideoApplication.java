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

//	static {
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//	}
	public static void main(String[] args) {
		SpringApplication.run(MakeVideoApplication.class, args);
		// Configuração básica do vídeo
//		String outputFile = "output.avi";
//		int width = 640;
//		int height = 480;
//		int fps = 30;
//
//		int codec = VideoWriter.fourcc('X', 'V', 'I', 'D');
//		// Inicializar o VideoWriter
//		VideoWriter videoWriter = new VideoWriter(
//				outputFile,
//				codec, // Codec (você pode usar "MP4V", "XVID", etc.)
//				fps,
//				new Size(width, height),
//				true // true para vídeo colorido
//		);
//
//		if (!videoWriter.isOpened()) {
//			System.out.println("Erro ao abrir o VideoWriter");
//			return;
//		}
//
//		// Criar quadros e adicioná-los ao vídeo
//		for (int i = 0; i < fps * 5; i++) { // 5 segundos de vídeo
//			Mat frame = new Mat(height, width, CvType.CV_8UC3, new Scalar(0, 0, 255)); // Vermelho
//			Imgproc.putText(
//					frame,
//					"Frame " + i,
//					new Point(50, 50),
//					Imgproc.FONT_HERSHEY_SIMPLEX,
//					1.0,
//					new Scalar(255, 255, 255), // Cor do texto
//					2
//			);
//			videoWriter.write(frame); // Adiciona o quadro ao vídeo
//			frame.release(); // Libera memória
//		}
//
//		// Finaliza a gravação
//		videoWriter.release();
//		System.out.println("Vídeo criado: " + outputFile);
//	}
}}
