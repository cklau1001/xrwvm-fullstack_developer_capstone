package io.cklau1001.capstone.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Read the OpenRouter settings from properties file
 *
 */
@Component
@ConfigurationProperties(prefix = "spring.ai.openrouter")
@Getter
@Setter
public class OpenRouterProperties {

    private String baseUrl;
    private String completionPath;
    private String embeddingPath;
    private Chat chat;

    @Getter
    @Setter
    public static class Chat {
        private Options options;
    }

    @Getter
    @Setter
    public static class Options {
        private int maxTokens;
        private Set<String> models;
    }

    public String[] getModels() {

        return chat.options.models.toArray(new String[0]);
    }

}
