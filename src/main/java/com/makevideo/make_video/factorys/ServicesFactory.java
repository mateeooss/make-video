package com.makevideo.make_video.factorys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ServicesFactory {
    private static ApplicationContext applicationContext;

    @Autowired
    public ServicesFactory(ApplicationContext applicationContext) {
        ServicesFactory.applicationContext = applicationContext;
    }

    public static <T> T getService(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }
}
