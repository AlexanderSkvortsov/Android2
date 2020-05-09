package ru.geekbrains.lesson1.net;

import java.util.HashMap;
import java.util.Map;

public class Sys {

    private String pod;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
