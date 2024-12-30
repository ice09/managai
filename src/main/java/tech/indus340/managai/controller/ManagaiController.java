package tech.indus340.managai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.indus340.managai.agentflow.AgentFlowEngine;

@RestController
public class ManagaiController {

    private final AgentFlowEngine agentFlowEngine;

    public ManagaiController(AgentFlowEngine agentFlowEngine) {
        this.agentFlowEngine = agentFlowEngine;
    }

    @GetMapping("/managai")
    public ResponseEntity<String> queryMcp(@RequestParam("message") String message) {
        return ResponseEntity.ofNullable(agentFlowEngine.agentFlow(message));
    }
}
