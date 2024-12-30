package tech.indus340.managai.chatbot;

import org.springframework.stereotype.Service;

@Service
public class OrchestratorAgent {

    private final OrchestratorAgentConfig config;

    public OrchestratorAgent(OrchestratorAgentConfig config) {
        this.config = config;
    }

    public String chat(String userMessage) {
        return config.orchestrationModel(userMessage).choices().getFirst().message().content().get();
    }

}