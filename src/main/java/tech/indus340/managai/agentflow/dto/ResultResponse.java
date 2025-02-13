package tech.indus340.managai.agentflow.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * A record representing the entire JSON object.
 * The JSON object now contains two properties: "userMessage" and "agents".
 */
public record ResultResponse(String userMessage, List<AgentResponse> agents) {

    @JsonCreator
    public ResultResponse(
            @JsonProperty("userMessage") String userMessage,
            @JsonProperty("agents") List<AgentResponse> agents) {
        this.userMessage = userMessage;
        this.agents = agents;
    }

    /**
     * A record representing an individual agent's response.
     * Each agent object has keys: "name", "model", "system_message", and "temperature".
     */
    public record AgentResponse(
            @JsonProperty("name") String name,
            @JsonProperty("model") String model,
            @JsonProperty("system_message") String systemMessage,
            @JsonProperty("temperature") Double temperature
    ) {}
}
