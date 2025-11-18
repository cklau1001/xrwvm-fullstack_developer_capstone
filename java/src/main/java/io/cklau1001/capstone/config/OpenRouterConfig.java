package io.cklau1001.capstone.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Configuration
public class OpenRouterConfig {

    private final String apiKey;
    private final OpenRouterProperties openRouterProperties;

    public OpenRouterConfig(@Value("${spring.ai.openai.api-key}") String apiKey,
                            OpenRouterProperties openRouterProperties) {

        this.apiKey = apiKey;
        this.openRouterProperties = openRouterProperties;
    }

    /**
     * Create OpenAiApi programmatically to customize for OpenRouter payload with backup models by using a custom
     * RestClient
     *
     * @return
     */
    @Bean
    public OpenAiApi openAiApi() {

        SimpleClientHttpRequestFactory simpleFactory = new SimpleClientHttpRequestFactory();
        BufferingClientHttpRequestFactory bufferingFactory = new BufferingClientHttpRequestFactory(simpleFactory);
        RestClient.Builder restClientBuilder = RestClient
                .builder()
                .baseUrl(openRouterProperties.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("header1", "abc")
                .requestFactory(bufferingFactory)
                .requestInterceptor((request, body, execution) -> {
                            log.info("[inside interceptor]: entered");

                            ObjectMapper objectMapper = new ObjectMapper();
                            TypeReference<Map<String, Object>> jsonMapRef = new TypeReference<Map<String, Object>>() {};
                            Map<String, Object> jsonMap = objectMapper.readValue(body, jsonMapRef);

                            // add backup models for OpenRouter chat completion endpoint
                            jsonMap.put("models", openRouterProperties.getModels());
                            log.info("[inside interceptor]: After adding models, jsonMap={}", jsonMap);

                            body = objectMapper.writeValueAsBytes(jsonMap);
                            String bodyStr = new String(body, StandardCharsets.UTF_8);
                            log.info("[inside interceptor]: After writing back to body, bodyStr={}", bodyStr);
                            return execution.execute(request, body);
                        }
                );

        log.info("[openAiApi]: Create OpenAiApi instance");
        return OpenAiApi.builder()
                .baseUrl(openRouterProperties.getBaseUrl())
                .apiKey(apiKey)
                .completionsPath(openRouterProperties.getCompletionPath())
                .embeddingsPath(openRouterProperties.getEmbeddingPath())
                .restClientBuilder(restClientBuilder)
                .build();

    }


}
