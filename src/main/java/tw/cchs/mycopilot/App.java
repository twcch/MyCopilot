package tw.cchs.mycopilot;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class App {

    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) {

        String chineseText = "2024年夏季奧林匹克運動會，於2024年7月26日至8月11日在法國巴黎舉行";

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "你是一位翻譯助手，請幫我將中文翻譯成英文。"));
        messages.add(new Message("user", chineseText));

        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.addProperty("model", "gpt-4o-mini");
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

        System.out.println(translation);

    }

}
