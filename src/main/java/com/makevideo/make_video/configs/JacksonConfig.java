package com.makevideo.make_video.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.makevideo.make_video.models.video_template.VideoTemplate;
import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();

        // Criar e registrar um SimpleModule
        SimpleModule module = new SimpleModule();

        // Usar o Reflections para encontrar todas as subclasses de VideoTemplate
        Reflections reflections = new Reflections("com.makevideo.make_video.models.video.impl"); // Ajuste o pacote conforme sua aplicação

        // Encontrar todas as subclasses de VideoTemplate
        Set<Class<? extends VideoTemplate>> subTypes = reflections.getSubTypesOf(VideoTemplate.class);

        // Registrar dinamicamente os subtipos
        for (Class<? extends VideoTemplate> subType : subTypes) {
            String typeName = subType.getSimpleName();  // Usando o nome da classe como nome do tipo
            module.registerSubtypes(new NamedType(subType, typeName));
        }

        // Registrar o SimpleModule com o ObjectMapper
        objectMapper.registerModule(module);

        return objectMapper;
    }
}
