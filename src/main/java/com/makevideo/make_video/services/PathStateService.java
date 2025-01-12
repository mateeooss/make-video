package com.makevideo.make_video.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PathStateService {
    @Value("${basePath}")
    public String basePath;
    public String directoryPath;
    public String txtPath;
}
