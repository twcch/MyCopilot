package tw.cchs.mycopilot.model;

import java.util.ArrayList;
import java.util.List;

public class ChatGPTModel {

    private List<String> modelNames;
    private String currentModel;

    public ChatGPTModel() {
        init();
        setCurrentModel("gpt-3.5-turbo");
    }

    public ChatGPTModel(String model) {
        init();
        setCurrentModel(model);
    }

    private void init() {
        modelNames = new ArrayList<>();
        modelNames.add("gpt-3.5-turbo");
        modelNames.add("gpt-4");
        modelNames.add("gpt-4o");
        modelNames.add("gpt-4o-mini");
        modelNames.add("o1-preview");
        modelNames.add("o1-mini");
    }

    // 添加新模型
    public void addModel(String modelName) {
        if (!modelNames.contains(modelName)) {
            modelNames.add(modelName);
        }
    }

    // 移除模型
    public void removeModel(String modelName) {
        modelNames.remove(modelName);
    }

    // 設定當前模型
    public void setCurrentModel(String modelName) {
        if (!modelNames.contains(modelName)) {
            throw new IllegalArgumentException("modelName not exist");
        }
        currentModel = modelName;
    }

    // 取得當前模型
    public String getCurrentModel() {
        return currentModel;
    }

    // 取得模型列表
    public List<String> getAllModel() {
        return modelNames;
    }

}
