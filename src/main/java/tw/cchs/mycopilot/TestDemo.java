package tw.cchs.mycopilot;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import tw.cchs.mycopilot.model.Message;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class TestDemo {

    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final int BATCH_SIZE = 1000;

    public static void main( String[] args )
    {
        String inputFilePath = "src/main/resources/input.txt";
        String outputFilePath = "src/main/resources/output.txt";
        List<String> batches = new ArrayList<>();
        StringBuilder batch = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (batch.length() + line.length() > BATCH_SIZE) {
                    batches.add(batch.toString());
                    batch = new StringBuilder();
                }
                batch.append(line).append("\n");
            }
            if (batch.length() > 0) {
                batches.add(batch.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> translations = new ArrayList<>();
        int count = 1;
        for (String batchTmp : batches) {
            System.out.println("translate batch No."+ count);
            String translation = translateText(batchTmp);
            translations.add(translation);
            count++;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (String translation : translations) {
                writer.write(translation);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Translation complete. Check " + outputFilePath);

    }
    private static String translateText(String text) {
        try {
            List<Message> messages = new ArrayList<>();
            messages.add(new Message("system", "你是一位翻譯助手，請將英文翻譯成繁體中文。"));
            messages.add(new Message("user", text));
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
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            String translation = jsonResponse.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();
            return translation;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

}
