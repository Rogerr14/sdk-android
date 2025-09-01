package com.nuvei.nuveisdk.model;

public class ErrorData {
    private String type;
    private String help;
    private String description;

    public ErrorData(String type, String help, String description) {
        this.type = type;
        this.help = help;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
