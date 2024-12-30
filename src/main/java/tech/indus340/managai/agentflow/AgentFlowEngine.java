package tech.indus340.managai.agentflow;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import tech.indus340.managai.chatbot.OrchestratorAgent;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AgentFlowEngine {

    private final OrchestratorAgent orchestratorAgent;
    private final ObjectMapper objectMapper;
    private final WorkerAgentFactory workerAgentFactory;

    public AgentFlowEngine(OrchestratorAgent orchestratorAgent, ObjectMapper objectMapper, WorkerAgentFactory workerAgentFactory) {
        this.orchestratorAgent = orchestratorAgent;
        this.objectMapper = objectMapper;
        this.workerAgentFactory = workerAgentFactory;
    }

    public String agentFlow(String userMessage) {
        String resultJson = orchestratorAgent.chat(userMessage);
        System.out.println(resultJson);
        StringBuilder answers = new StringBuilder("USERMESSAGE: " + userMessage + "\n###\n");
        try {
            // Parse the JSON string into a LinkedHashMap to maintain the order of keys
            Map<String, Map<String, String>> resultMap =
                    objectMapper.readValue(resultJson, new TypeReference<LinkedHashMap<String, Map<String, String>>>() {});

            for (Map.Entry<String, Map<String, String>> entry : resultMap.entrySet()) {
                String systemMessage = entry.getValue().get("system_message");
                String modelName = entry.getValue().get("model_name");
                double temp = Double.parseDouble(entry.getValue().get("temperature"));
                answers.append("\n###\nANSWER " + entry.getKey() + "\n###\n" + workerAgentFactory.build(systemMessage, modelName, temp).chat(answers.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answers.toString();
    }

}
