package io.cklau1001.capstone.service;

import io.cklau1001.capstone.dto.SentimentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * A service to interact with external LLM model endpoint
 *
 */

@Slf4j
@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * Return sentiment of the incoming statement, dealerId and reviewId are used for caching purpose.
     *
     * @param dealerId
     * @param reviewId
     * @param statement
     * @return
     */
    @Cacheable(value = "reviews", key="'sentiment/dealer_id=' + #dealerId + '/reviewId=' + #reviewId")
    public SentimentResponse analyzeSentiment(int dealerId, int reviewId, String statement) {

        log.debug("[analyzeSentiment]: entered, dealerId={}, reviewId={}", dealerId, reviewId);
        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                .maxTokens(20)
                .build();

        log.info("[analzyeSentiment]: providing sentiment for dealerId={}, reviewId={}", dealerId, reviewId);
        return chatClient.prompt()
                .user(statement)
                .options(chatOptions)
                .call()
                .entity(SentimentResponse.class);
    }

}
