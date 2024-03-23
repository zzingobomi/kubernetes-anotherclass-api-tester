package com.pro.app.controller;

import com.pro.app.component.FileUtils;
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

    @Autowired
    private FileUtils fileUtils;

    @Value(value = "${downward.volume.filepath}")
    private String downwardApiVolumeFilepath;

    @Value(value = "${downward.env.pod-name}")
    private String downwardApiEnvPodName;
    @Value(value = "${downward.env.pod-ip}")
    private String downwardApiEnvPodIP;
    @Value(value = "${downward.env.node-name}")
    private String downwardApiEnvNodeName;

    @Value(value = "${api-token.url}")
    private String apiTokenUrl;

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

        String returnYaml = sprint3Service.getSelfPodKubeApiServer(apiTokenUrl, downwardApiEnvPodName, apiTokenFilepath);
        if (returnYaml != null) {
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
        }  else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/graceful-shutdown")
    public void gracefulShutdown() {
        // 내부의 종료 로직 호출
        System.exit(1);

        // 이후 Shutdwon Hook 컴포넌트에서 자원 해제 로직이 실행됨
    }

    @GetMapping("/unexpected-shutdown")
    public void unexpectedShutdown() {
        try {
            throw new RuntimeException("The system has been shut down due to a memory leak.");
        } catch (RuntimeException e) {

            // 종료 메세지가 terminationMessagePath에 저장됨
            fileUtils.writeTerminationMessage(e.getMessage());
        }
    }
}