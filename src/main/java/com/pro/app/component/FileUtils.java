package com.pro.app.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;

@Component
public class FileUtils {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public String readFile(String path) {
        try {
            // 파일의 내용을 문자열로 반환
            return new String(java.nio.file.Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            // 파일 읽기 오류 처리
            log.error("Read Error: " + e.getMessage());
            return null;
        }
    }
}
