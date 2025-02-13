package tech.indus340.managai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.indus340.managai.agentflow.AgentFlowEngine;
import tech.indus340.managai.agentflow.dto.ResultResponse;

@RestController
public class ManagaiController {

    private final AgentFlowEngine agentFlowEngine;

    public ManagaiController(AgentFlowEngine agentFlowEngine) {
        this.agentFlowEngine = agentFlowEngine;
    }

    @GetMapping("/managai")
    public ResponseEntity<ResultResponse> queryMcp(@RequestParam("message") String message) throws JsonProcessingException {
        return ResponseEntity.ofNullable(agentFlowEngine.agentFlow(message));
    }

    @PostMapping("/managai")
    public ResponseEntity<String> executeMcp(@RequestBody ResultResponse processFlow) {
        return ResponseEntity.ofNullable(agentFlowEngine.executeFlow(processFlow));
    }
}
