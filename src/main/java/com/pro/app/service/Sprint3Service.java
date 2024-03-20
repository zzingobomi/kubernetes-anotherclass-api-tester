package com.pro.app.service;


import com.pro.app.domain.DatasourceProperties;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.ClientBuilder;

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
        String namespace = path + "namespace";
        String responseString = "";

        try {
        // ApiClient 구성
        ApiClient client = Config.defaultClient();

        // Kubernetes API 서버의 도메인 주소 설정
        String kubeApiServerUrl = "https://kubernetes.default";
        client.setBasePath(kubeApiServerUrl);

        // 토큰과 CA 인증서로 인증 구성
        String token = new String(java.nio.file.Files.readAllBytes(Paths.get(tokenPath)));
        client.setApiKey("Bearer " + token);
        client.setSslCaCert(new java.io.FileInputStream(caPath));

        // 설정 적용
        Configuration.setDefaultApiClient(client);

        // Kubernetes API 호출
        CoreV1Api api = new CoreV1Api();
        V1Pod pod = api.readNamespacedPod(podName, namespace, "true");
        responseString = Yaml.dump(pod);


        } catch (ApiException e) {
            log.error("Status: " + e.getCode());
            log.error("Body: " + e.getResponseBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseString;
    }



}
