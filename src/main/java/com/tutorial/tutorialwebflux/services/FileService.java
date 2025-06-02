package com.tutorial.tutorialwebflux.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.multipart.MultipartFile;

@RequestScope
@Component
public class FileService {
    
    private String pathFile = "D:\\Softaware\\Github\\Repositorios\\springboot\\spring-native-cloud-files-tomcat\\upload\\";
    private Logger logger = LoggerFactory.getLogger(FileService.class);

    public String uploadFile(MultipartFile file) throws IOException{

        file.transferTo(new File(pathFile+file.getOriginalFilename()));
        logger.info("Archivo cargado: {} ", pathFile);
        return pathFile+file.getOriginalFilename();
    }

    public List<String> readFile(MultipartFile file) throws Exception{
        List<String> lines = new ArrayList<>();
        //Files f = file.getInputStream();
        InputStream inputStream = file.getInputStream();
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        } catch (Exception e) {
            logger.error("An error ocurred: {}", e.getMessage());
            throw new Exception(e.getMessage());
        }
       return lines;
    }
}
