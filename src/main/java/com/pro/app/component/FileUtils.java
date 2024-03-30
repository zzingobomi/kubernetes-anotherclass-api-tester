package com.pro.app.component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
public class FileUtils {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Value("${termination.message-path}")
    private String terminationMessagePath;

    public void writeTerminationMessage(String message) {

        Path messagePath = Path.of(terminationMessagePath);
        try {

            // 폴더 없으면 생성
            Path parentDir = messagePath.getParent();
            if ( parentDir == null ) {
                return;
            }

            File path = new File(parentDir.toString());
            if(!path.exists()) {
                path.mkdirs();
            }

            // 파일 쓰기 (파일이 이미 있으면 대체, 없으면 생성)
            Files.writeString(messagePath, message, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);


        } catch (IOException e) {
            log.error("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
