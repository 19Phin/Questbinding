package net.dialingspoon.questbind.util;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class ButtonBind {
    public String sensoryType;
    public String output;
    public Map<String, String> parameters;
    public String mode;
    public String button;
    public String path;
    public ButtonBind(String output, String button) {
        this.sensoryType = "click";
        this.output = output;
        this.parameters = new HashMap<>();
        this.mode = "button";
        this.button = button;
        this.path = "ingame";
    }
    public ButtonBind(String sensoryType, String output, Map<String, String> parameters, String mode, String button, String path) {
        this.sensoryType = sensoryType;
        this.output = output;
        this.parameters = parameters;
        this.mode = mode;
        this.button = button;
        this.path = path;
    }
    public JsonObject asJson() {

        JsonObject json = new JsonObject();
        if (!sensoryType.isBlank()) {
            json.addProperty("output", "/actions/" + path + "/in/" + output);
            json = nestJson(sensoryType, json);
        }
        json = nestJson("inputs", json);
        if (!parameters.isEmpty()) {
            JsonObject parameters = new JsonObject();
            for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
                parameters.addProperty(entry.getKey(), entry.getValue());
            }
            json.add("parameters", parameters);
        }
        json.addProperty("mode", mode);
        json.addProperty("path", "/user/hand/"+button);
        return json;
    }

    public JsonObject nestJson(String property, JsonObject json) {
        JsonObject tempJson = new JsonObject();
        tempJson.add(property, json);
        return tempJson;
    }

    public boolean isDefault() {
        return (sensoryType.equals("click")
        && parameters.isEmpty()
        && mode.equals("button")
        && path.equals("ingame"));
    }
}