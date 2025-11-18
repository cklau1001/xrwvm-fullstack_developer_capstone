package io.cklau1001.capstone.service;

import io.cklau1001.capstone.dto.SentimentResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    private ChatService chatService;

    @Mock
    private ChatClient mockChatClient;

    @Mock
    private ChatClient.Builder mockChatClientBuilder;

    @Mock
    private ChatClient.ChatClientRequestSpec mockChatRequestSpec;

    @Mock
    private ChatClient.CallResponseSpec mockChatResponseSpec;

    @BeforeEach
    void init() {
        log.info("[init]: entered");
        when(mockChatClientBuilder.build()).thenReturn(mockChatClient);
        chatService = new ChatService(mockChatClientBuilder);
    }

    @Test
    void hello() {
        System.out.println("kkk");
    }

    @Test
    void sentimentAnalyzeSuccess() {
        // when(mockChatClientBuilder.build()).thenReturn(mockChatClient);
        when(mockChatClient.prompt()).thenReturn(mockChatRequestSpec);
        when(mockChatRequestSpec.user(anyString())).thenReturn(mockChatRequestSpec);
        when(mockChatRequestSpec.options(any())).thenReturn(mockChatRequestSpec);
        when(mockChatRequestSpec.call()).thenReturn(mockChatResponseSpec);

        SentimentResponse mockResponse = new SentimentResponse(0.5, "positive");
        when(mockChatResponseSpec.entity(SentimentResponse.class)).thenReturn(mockResponse);

        SentimentResponse result = chatService.analyzeSentiment(1, 5, "good service");

        // verify the mock below
        verify(mockChatClient, times(1)).prompt();
        verify(mockChatRequestSpec, times(1)).user(anyString());
        verify(mockChatRequestSpec, times(1)).options(any());
        verify(mockChatRequestSpec, times(1)).call();

        assertThat(result).isEqualTo(mockResponse);

    }
}
