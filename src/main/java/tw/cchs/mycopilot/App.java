package tw.cchs.mycopilot;

import tw.cchs.mycopilot.model.ChatGPTModel;
import tw.cchs.mycopilot.openai.ChatGPT;

public class App {

    public static void main(String[] args) {

        ChatGPT chatGPT = new ChatGPT(new ChatGPTModel());
        System.out.println(chatGPT.getSystemRole());

    }

}
