package com.example.hackaton;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.models.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OpenAiService {
    private static final String OPENAI_ENDPOINT = "https:// Open AI API endpoint";
    private static final String DEPLOYMENT_ID = "Open AI deployment name";
    private final String OPENAI_KEY = "one of Open AI API keys";

    // Chat completions API https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#chat-completions
    public String chatCompletions(String message) {
        log.info("message: {}", message);

        OpenAIClient client = getOpenAIClient();

        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.USER, message));

        ChatCompletions chatCompletions = client.getChatCompletions(DEPLOYMENT_ID, new ChatCompletionsOptions(chatMessages));

        String result = "";
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatMessage chatMessage = choice.getMessage();
            result = chatMessage.getContent();
        }

        log.info("result: {}", result);
        return result;
    }

    public String generateImages(String message) {
        OpenAIClient client = getOpenAIClient();

        ImageGenerationOptions imageGenerationOptions = new ImageGenerationOptions(message);
        ImageResponse images = client.getImages(imageGenerationOptions);

        String result = null;
        for (ImageLocation imageLocation : images.getData()) {
            ResponseError error = imageLocation.getError();
            if (error != null) {
                log.error("Image generation operation failed. Error code: {}, error message: {}",
                        error.getCode(), error.getMessage());
            } else {
                log.info("Image location URL that provides temporary access to download the generated image is {}",
                        imageLocation.getUrl());
                result = imageLocation.getUrl();
            }
        }
        return result;
    }

    private OpenAIClient getOpenAIClient() {
        return new OpenAIClientBuilder()
                .endpoint(OPENAI_ENDPOINT)
                .credential(new AzureKeyCredential(OPENAI_KEY))
                .buildClient();
    }
}
