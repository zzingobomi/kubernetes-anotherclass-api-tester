package com.pro.app.component;



import com.pro.app.service.Sprint3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ShutdownHook {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Value(value = "${termination.message-path}")
    private String terminationMessagePath;

    @Autowired
    private FileUtils fileUtils;

    @PreDestroy
    public void cleanup() {

        // 현재 시간을 LocalDateTime 객체로 가져옴
        LocalDateTime now = LocalDateTime.now();

        // DateTimeFormatter를 사용하여 시간 포맷 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 현재 시간을 설정한 포맷으로 출력
        log.info("Current time: " + now.format(formatter));

        try {
            log.info("Database connection has been safely released.");
            Thread.sleep(1000);
            log.info("Current time: " + now.format(formatter));

            log.info("File stream has been safely released.");
            Thread.sleep(1000);
            log.info("Current time: " + now.format(formatter));

            log.info("Message Queue has been safely released.");
            Thread.sleep(1000);
            log.info("Current time: " + now.format(formatter));

            log.info("Thread is safely releasing...");
            Thread.sleep(1000);
            log.info("Current time: " + now.format(formatter));

            log.info("Running Thread... (4/5)");
            Thread.sleep(2000);
            log.info("Current time: " + now.format(formatter));

            log.info("Running Thread... (3/5)");
            Thread.sleep(2000);
            log.info("Current time: " + now.format(formatter));

            log.info("Running Thread... (2/5)");
            Thread.sleep(2000);
            log.info("Current time: " + now.format(formatter));

            log.info("Running Thread... (1/5)");
            Thread.sleep(2000);
            log.info("Current time: " + now.format(formatter));

            log.info("Thread has been safely released.");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 종료 메세지가 terminationMessagePath에 저장됨
        fileUtils.writeTerminationMessage("The application shuts down gracefully");
    }
}
