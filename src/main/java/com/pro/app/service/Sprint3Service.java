package com.pro.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pro.app.domain.DatasourceProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

@Service
public class Sprint3Service {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private DatasourceProperties datasourceProperties;



    public String loadDownwardApiFile(String path) throws Exception  {
        Yaml y = new Yaml();
        Reader yamlFile = new FileReader(path);

        Map<String, Object> yamlMaps = y.load(yamlFile);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String prettyYamlString = mapper.writeValueAsString(yamlMaps);

        return prettyYamlString;
    }
}
