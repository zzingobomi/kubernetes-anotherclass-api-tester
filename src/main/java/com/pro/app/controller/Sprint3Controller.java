package com.pro.app.controller;

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

    @Value(value = "${api-token.filepath}")
    private String apiTokenFilepath;

    public Sprint3Controller() {
    }

    @GetMapping("/pod-downward-api-volume")
    @ResponseBody
    public ResponseEntity<Object> podDownwardApiVolume()  {

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

    @GetMapping("/pod-kube-api-server")
    @ResponseBody
    public ResponseEntity<Object> podKubeApiServer()  {

        String returnYaml = sprint3Service.getSelfPodKubeApiServer(downwardApiEnvPodName, apiTokenFilepath);
        String escapeHtml = returnYaml.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");

        // YAML 문자열을 HTML 내에 포함
        String htmlContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<title>Pod Information</title>" +
                "<style>pre { background-color: #f0f0f0; padding: 10px; }</style>" +
                "</head>" +
                "<body>" +
                "<h1>Pod Information in YAML Format</h1>" +
                "<pre>" + escapeHtml + "</pre>" +
                "</body>" +
                "</html>";

        return ResponseEntity.ok().header("Content-Type", "text/html;charset=UTF-8")
                .body(htmlContent);
    }

}
