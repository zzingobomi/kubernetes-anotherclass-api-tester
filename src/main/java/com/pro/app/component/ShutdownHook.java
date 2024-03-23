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

@Component
public class ShutdownHook {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Value(value = "${termination.message-path}")
    private String terminationMessagePath;

    @Autowired
    private FileUtils fileUtils;

    @PreDestroy
    public void cleanup() {


        try {
            Thread.sleep(2000);
            log.info("Database connection has been safely released.");

            Thread.sleep(2000);
            log.info("File stream has been safely released.");

            Thread.sleep(2000);
            log.info("Thread has been safely released.");

            Thread.sleep(2000);
            log.info("Message Queue has been safely released.");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 종료 메세지가 terminationMessagePath에 저장됨
        fileUtils.writeTerminationMessage("The application shuts down gracefully");
    }
}
