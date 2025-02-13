# Managai: The Many-Agent-AI

Managai is a demo for dynamic LLM agent creation and execution.

<img src="docs/manyagentai.webp" width="400"/>

One orchestrator agent takes the user command and spawns multiple agents with inferior models to execute single, simple tasks. At the end of the process flow, one agent will summarize and merge the input by the other agents.

<img src="docs/agentflow.png" width="400"/>

The idea is that the orchestrator agent should answer with strictly valid JSON, which can then be interpreted by a agent flow engine to dynamically create and execute the worker agents.

## Prerequisites

* Java 21

## Run

* Start `./gradlew bootRun`
* Open `http://localhost:8080/`