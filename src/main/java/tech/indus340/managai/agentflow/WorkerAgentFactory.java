package tech.indus340.managai.agentflow;

import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Service;
import tech.indus340.managai.chatbot.WorkerAgent;
import tech.indus340.managai.chatbot.WorkerAgentConfig;

@Service
public class WorkerAgentFactory {

    private final WorkerAgentConfig workerAgentConfig;

    public WorkerAgentFactory(WorkerAgentConfig workerAgentConfig) {
        this.workerAgentConfig = workerAgentConfig;
    }

    public WorkerAgent build(String systemMessage, String modelName, double temp) {
        return AiServices.builder(WorkerAgent.class)
                .chatLanguageModel(workerAgentConfig.workerModel(modelName, temp))
                .systemMessageProvider(o -> systemMessage)
                .build();
    }
}
