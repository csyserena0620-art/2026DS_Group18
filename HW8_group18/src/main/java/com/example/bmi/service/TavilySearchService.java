package com.example.bmi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class TavilySearchService {

    @Value("${tavily.enabled:false}")
    private boolean enabled;

    @Value("${tavily.apiKey:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Map<String, String>> search(String query) {
        List<Map<String, String>> results = new ArrayList<>();

        if (!enabled || apiKey == null || apiKey.isBlank()) {
            return results;
        }

        String trimmedQuery = query == null ? "" : query.trim();
        if (trimmedQuery.isEmpty()) {
            return results;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("query", trimmedQuery);
        body.put("max_results", 10);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.tavily.com/search",
                    request,
                    Map.class
            );

            Map<String, Object> bodyResponse = response.getBody();
            if (bodyResponse == null) {
                return results;
            }

            Object itemsObj = bodyResponse.get("results");
            if (!(itemsObj instanceof List<?> items)) {
                return results;
            }

            for (Object itemObj : items) {
                if (!(itemObj instanceof Map<?, ?> itemMap)) {
                    continue;
                }

                Object titleObj = itemMap.get("title");
                Object urlObj = itemMap.get("url");
                Object contentObj = itemMap.get("content");

                String title = titleObj == null ? "" : titleObj.toString();
                String url = urlObj == null ? "" : urlObj.toString();
                String content = contentObj == null ? "" : contentObj.toString();

                if (!title.isBlank() && !url.isBlank()) {
                    Map<String, String> item = new LinkedHashMap<>();
                    item.put("title", title);
                    item.put("url", url);
                    item.put("content", content);
                    results.add(item);
                }
            }
        } catch (RestClientException ignored) {
            return results;
        }

        return results;
    }
}
