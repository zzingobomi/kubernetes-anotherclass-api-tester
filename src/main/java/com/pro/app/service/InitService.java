package com.pro.app.service;

import com.pro.app.component.CpuLoad;
import com.pro.app.component.ObjectForLeak;
import com.pro.app.domain.DatasourceProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InitService {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private DatasourceProperties datasourceProperties;

    @Value(value = "${postgresql.filepath}")
    private String postgresqlFilepath;

    @PostConstruct
    public void datasourceSecretLoad() {
        Yaml y = new Yaml();
        Reader yamlFile = null;
        try {
            yamlFile = new FileReader(postgresqlFilepath);
        } catch (FileNotFoundException e) {

        }

        if (yamlFile != null) {
            Map<String, Object> yamlMaps = y.load(yamlFile);

            datasourceProperties.setDriverClassName(yamlMaps.get("driver-class-name").toString());
            datasourceProperties.setUrl(yamlMaps.get("url").toString());
            datasourceProperties.setUsername(yamlMaps.get("username").toString());
            datasourceProperties.setPassword(yamlMaps.get("password").toString());
        }
    }
}
