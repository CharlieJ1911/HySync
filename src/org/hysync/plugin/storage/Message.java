package org.hysync.plugin.storage;

public enum Message {
    WELCOME_ADD_KEY("&aHey there! This is your first time running HySync. In order to work properly, we require a valid Hypixel API key." +
            "\n&ePlease enter your Hypixel API key in chat, or type '&ccancel&e' to cancel."),
    WELCOME_VALID_KEY("&eKey set to &a%key%");

    private final String message;
    private Message(String message) {
        this.message = message;
    }

    public String[] getMsg(){
        return message.split("\n");
    }
}
