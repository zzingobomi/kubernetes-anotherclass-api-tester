package com.pro.app.controller;

import com.pro.app.domain.DatasourceProperties;
import com.pro.app.service.DefaultService;
import com.pro.app.service.Sprint3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Sprint3Controller {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    private Sprint3Service sprint3Service;

    @Value(value = "${downward-api.volume.filepath}")
    private String downwardApiVolumeFilepath;

    @Value(value = "${downward-api.env.podname}")
    private String downwardApiEnvPodName;
    @Value(value = "${downward-api.env.podip}")
    private String downwardApiEnvPodIP;
    @Value(value = "${downward-api.env.nodename}")
    private String downwardApiEnvNodeName;


    public Sprint3Controller() {
    }

    @GetMapping("/pod-downward-api-volume")
    @ResponseBody
    public ResponseEntity<Object> podDownwardApiVolume() throws Exception {

        String returnString = sprint3Service.loadDownwardApiFile(downwardApiVolumeFilepath);
        return ResponseEntity.ok(returnString);
    }

    @GetMapping("/pod-downward-api-env")
    @ResponseBody
    public ResponseEntity<Object> podDownwardApiEnv() {
        String returnString = "<b>[Pod Name] :</b> " + downwardApiEnvPodName ;
        returnString += "<br><b>[Pod IP] :</b> " + downwardApiEnvPodIP;
        returnString += "<br><b>[Node Name] :</b> " + downwardApiEnvNodeName ;
        return ResponseEntity.ok(returnString);
    }

}
