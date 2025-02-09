# Managai: The Many-Agent-AI

Managai is a demo for dynamic LLM agent creation and execution.

![manyagentai.webp](docs/manyagentai.webp)

One orchestrator agent takes the user command and spawns multiple agents with inferior models to execute single, simple tasks. At the end of the process flow, one agent will summarize and merge the input by the other agents.

![image](https://github.com/user-attachments/assets/6d9f57a3-975a-43c2-9091-02130d8e5376)

The idea is that the orchestrator agent should answer with strictly valid JSON, which can then be interpreted by a agent flow engine to dynamically create and execute the worker agents.
