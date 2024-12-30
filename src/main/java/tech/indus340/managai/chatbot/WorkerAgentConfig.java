package tech.indus340.managai.chatbot;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.time.Duration.ofSeconds;

@Service
public class WorkerAgentConfig {

    @Value("${openai.key}")
    private String openAIKey;

    public ChatLanguageModel workerModel(String modelName, double temp) {
        return OpenAiChatModel.builder()
                .apiKey(openAIKey)
                .modelName(modelName)
                .temperature(temp)
                .timeout(ofSeconds(360))
                .build();
    }

}