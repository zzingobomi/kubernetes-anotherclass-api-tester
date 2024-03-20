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
import java.nio.file.Paths;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Service
public class Sprint3Service {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private DatasourceProperties datasourceProperties;



    public String loadDownwardApiFile(String path)  {

        String allContents = "";

        StringBuilder allFileContentBuilder = new StringBuilder();

        // 폴더 내의 모든 파일 탐색
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) { // 파일인 경우만 처리
                    String fileContent = readFileAsString(file);
                    allFileContentBuilder.append(fileContent).append(System.lineSeparator());
                }
            }
        }
        allContents = allFileContentBuilder.toString();
        return allContents;
    }

    // 파일을 문자열로 읽어오는 메서드
    public String readFileAsString(File file) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public String getSelfPodKubeApiServer(String podName, String path) {

        // 토큰과 CA 인증서 경로 설정
        String tokenPath = path + "token";
        String caPath = path + "ca.crt";
        String namespace = path + "namespace";
        String responseString = "";

        try {
            // Kubernetes API 서버의 도메인 주소 설정
            String kubeApiServerUrl = "https://kubernetes.default";

            // 파일에서 Token 읽기
            String token = new String(Files.readAllBytes(Paths.get(tokenPath)));

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
