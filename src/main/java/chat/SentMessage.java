package chat;

public class SentMessage {

    private String text;

    public SentMessage() {
    }

    public SentMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}