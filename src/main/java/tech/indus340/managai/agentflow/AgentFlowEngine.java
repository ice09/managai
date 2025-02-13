package tech.indus340.managai.agentflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import tech.indus340.managai.agentflow.dto.ResultResponse;
import tech.indus340.managai.chatbot.OrchestratorAgent;

import java.util.logging.Logger;

@Service
public class AgentFlowEngine {

    private static final Logger LOGGER = Logger.getLogger(AgentFlowEngine.class.getName());

    private final OrchestratorAgent orchestratorAgent;
    private final ObjectMapper objectMapper;
    private final WorkerAgentFactory workerAgentFactory;

    public AgentFlowEngine(OrchestratorAgent orchestratorAgent, ObjectMapper objectMapper, WorkerAgentFactory workerAgentFactory) {
        this.orchestratorAgent = orchestratorAgent;
        this.objectMapper = objectMapper;
        this.workerAgentFactory = workerAgentFactory;
    }

    public ResultResponse agentFlow(String userMessage) throws JsonProcessingException {
        String resultJson = orchestratorAgent.chat(userMessage);
        LOGGER.info(resultJson);
        return objectMapper.readValue(resultJson, ResultResponse.class);
    }

    public String executeFlow(ResultResponse result) {
        StringBuilder answers = new StringBuilder("USERMESSAGE: " + result.userMessage() + "\n###\n");
        try {
            for (ResultResponse.AgentResponse agent : result.agents()) {
                String systemMessage = agent.systemMessage();
                String modelName = agent.model();
                double temp = agent.temperature() != null ? agent.temperature() : -1.0;
                answers.append("\n###\nANSWER ").append(agent.name()).append("\n###\n")
                        .append(workerAgentFactory.build(systemMessage, modelName, temp).chat(answers.toString())).append("\n");
            }
            LOGGER.info(answers.toString());
            return answers.toString();

        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
        return null;
    }

}
