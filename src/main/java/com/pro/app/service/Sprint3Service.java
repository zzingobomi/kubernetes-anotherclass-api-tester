package com.pro.app.service;


import com.pro.app.domain.DatasourceProperties;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Sprint3Service {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private DatasourceProperties datasourceProperties;



    public String loadDownwardApiFile(String path)  {

        String allContents = "";

        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            List<Path> fileList = paths.filter(Files::isRegularFile).collect(Collectors.toList());
            for (Path file : fileList) {
                allContents += "<b>File: " + file  +"</b><br>";
                List<String> fileContent = Files.readAllLines(file);
                for (String line : fileContent) {
                    allContents += line + "<br>" ;
                }
                allContents += "---<br>";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allContents;
    }


    public String getSelfPodKubeApiServer(String podName, String path) {

        // 토큰과 CA 인증서 경로 설정
        String tokenPath = path + "token";
        String caPath = path + "ca.crt";
        String namespacePath = path + "namespace";
        String responseString = "";

        try {
            // Kubernetes API 서버의 도메인 주소 설정
            String kubeApiServerUrl = "https://kubernetes.default";

            // 파일에서 Token 읽기
            String token = new String(Files.readAllBytes(Paths.get(tokenPath)));
            String namespace = new String(Files.readAllBytes(Paths.get(namespacePath)));

            // ApiClient 생성 및 설정
            ApiClient client = Config.defaultClient();
            client.setBasePath(kubeApiServerUrl);
            client.setApiKey("Bearer " + token);
            client.setSslCaCert(new java.io.FileInputStream(caPath));
            Configuration.setDefaultApiClient(client);

            // Kubernetes API 호출
            CoreV1Api api = new CoreV1Api();
            V1Pod pod = api.readNamespacedPod(podName, namespace, "true");
            responseString = Yaml.dump(pod);
            log.info("ResponseString: " + responseString);


        } catch (ApiException e) {
            log.error("Status: " + e.getCode());
            log.error("Body: " + e.getResponseBody());
            responseString = e.getResponseBody();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }



}
