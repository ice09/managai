package tech.indus340.managai.chatbot;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrchestratorAgentConfig {

    private final static int KONTEXT_WINDOW_MESSAGES_SIZE = 1;

    @Value("${openai.key}")
    private String openAIKey;

    public ChatCompletion orchestrationModel(String userMessage) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .messages(
                    List.of(
                        ChatCompletionMessageParam.ofChatCompletionUserMessageParam(
                            ChatCompletionUserMessageParam.builder()
                                .role(ChatCompletionUserMessageParam.Role.USER)
                                .content(ChatCompletionUserMessageParam.Content.ofTextContent(
                                        """
You are a powerful orchestration LLM which gets a user message request and instead of answering the request, you will split the task into different actions which execute different aspects of the question.
You will create the system message for these smaller agents. Your answer to a question should be a ONLY the JSON representation of multiple ids (can be short names) and associated system messages for the agents. Only the valid JSON format, no other markup. Each agent in the sequence will get the complete result of the agents before with information about the agent itself.
You can choose between "gpt-4o" and "gpt-4o-mini" as models, with "gpt-4o-mini" being the default. You also have to choose the temperature the agent should use as double between 0 and 1.
There will be an external process which will sequentially execute your agents and provide the following agents with all information of the former agents. So an aggregating verifier at the end might make sense to create the final answer. Also, for complex tasks you could create several agents which would have suffix ids "-1", "-2" and so on. In this case, the surrounding process will feed the next verifier all answers to verify.
The last agent in the sequence must provide the final result completely with all details, so as if only one agent was asked by the user. This has to be explicitly included in the system message for this last agent.

For example:

user request: "I want to plan an hour lesson for primary school kids to explain, mostly analog, the functionality of LLMs".

Sample output:
{
  "ResearchAgent": {
    "system_message": "You are an agent tasked with researching age-appropriate analogies to explain the functionality of Large Language Models (LLMs) to primary school children. Provide several simple and relatable analogies that can help young students understand how LLMs work."
    "model_name": "gpt4o-mini",
    "temperature": 0.3
  },
  "StructureAgent": {
    "system_message": "You are an agent responsible for designing the structure of a one-hour lesson aimed at explaining the functionality of LLMs to primary school kids. Using the analogies provided by the ResearchAgent, create a detailed lesson outline that includes an introduction, main explanation, interactive activities, and a conclusion."
    "model_name": "gpt4o-mini",
    "temperature": 0.4
  },
  "MaterialsAgent": {
    "system_message": "You are an agent in charge of creating teaching materials for the lesson plan developed by the StructureAgent. Develop slides, handouts, and any other necessary materials that effectively utilize the analogies and explanations to help primary school students understand LLMs."
    "model_name": "gpt4o-mini",
    "temperature": 0.3
  },
  "ActivitiesAgent": {
    "system_message": "You are an agent tasked with planning interactive activities to include in the lesson. Based on the lesson structure and materials from the previous agents, design engaging, analog-based activities that reinforce the concepts of how LLMs function."
    "model_name": "gpt4o-mini",
    "temperature": 0.4
  },
  "Verifier": {
    "system_message": "You are an agent responsible for reviewing and verifying the entire lesson plan for clarity, age-appropriateness, and effectiveness in explaining the functionality of LLMs to primary school children. Ensure that all components are coherent, all relevant details are mentioned and suggest improvements if necessary."
    "model_name": "gpt4o",
    "temperature": 0
  }
}

User Request: "I want to implement a Spring Boot application using gradle/kotlin. How to implement certain actions using GH actions?"

Sample Response:

{
  "IdeaAgent-1": {
    "system_message": "You are an agent tasked with generating ideas for implementing a Spring Boot application using Gradle and Kotlin, as well as integrating GitHub Actions for various actions. Provide suggestions for project setup, Gradle configurations, and GitHub Actions workflows."
    "model_name": "gpt4o",
    "temperature": 0.3
  },
  "IdeaAgent-2": {
    "system_message": "You are an agent tasked with generating ideas for implementing a Spring Boot application using Gradle and Kotlin, as well as integrating GitHub Actions for various actions. Provide suggestions for project setup, Gradle configurations, and GitHub Actions workflows."
    "model_name": "gpt4o-mini",
    "temperature": 0.5
  },
  "IdeaAgent-3": {
    "system_message": "You are an agent tasked with generating ideas for implementing a Spring Boot application using Gradle and Kotlin, as well as integrating GitHub Actions for various actions. Provide suggestions for project setup, Gradle configurations, and GitHub Actions workflows."
    "model_name": "gpt4o",
    "temperature": 0.2
  },
  "Verifier": {
    "system_message": "You are an agent responsible for reviewing and verifying the ideas generated by the IdeaAgents. Ensure that the proposed ideas are coherent, feasible, and effectively address the implementation of the Spring Boot application and the GitHub Actions workflows. Provide feedback and suggest improvements if necessary."
    "model_name": "gpt4o",
    "temperature": 0
  }
}
This is the user request message: """ + userMessage
                        )).build()))
                    )
                .model(ChatModel.O1_PREVIEW)
                .build();
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(openAIKey).build();
        return client.chat().completions().create(params);
    }

}
