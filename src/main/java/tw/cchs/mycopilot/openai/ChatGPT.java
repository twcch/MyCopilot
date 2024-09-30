package tw.cchs.mycopilot.openai;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import tw.cchs.mycopilot.model.ChatGPTModel;
import tw.cchs.mycopilot.model.Message;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ChatGPT {

    private final String API_KEY = System.getenv("OPENAI_API_KEY");
    private final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private ChatGPTModel chatGPTModel;
    private String model;
    List<Message> messages;
    String systemRole;

    public ChatGPT() {
        chatGPTModel = new ChatGPTModel();
        model = chatGPTModel.getCurrentModel();
        messages = new ArrayList<>();
        systemRole = "你是我的AI，助手要負責回答我的任何問題";
    }

    public String call(String content) {
        messages.add(new Message("system", systemRole));
        messages.add(new Message("user", content));

        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.addProperty("model", model);
        json.add("messages", gson.toJsonTree(messages));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(json)))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
        String translation = jsonResponse.getAsJsonArray("choices")
                .get(0)
                .getAsJsonObject()
                .getAsJsonObject("message")
                .get("content")
                .getAsString();

        return translation;

    }

    public String getAPI_KEY() {
        return API_KEY;
    }

    public String getOPENAI_API_URL() {
        return OPENAI_API_URL;
    }

    public String getModel() {
        return chatGPTModel.getCurrentModel();
    }

    public void setModel(String model) {
        chatGPTModel.setCurrentModel(model);
    }

    public String getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(String systemRole) {
        this.systemRole = systemRole;
    }

}
