package delta;

import java.util.Map;

public class Message {

    private final Map<Integer, Integer> offsets;
    private final String payload;

    public Message(Map<Integer, Integer> offsets, String payload) {
        this.offsets = offsets;
        this.payload = payload;
    }

    public Map<Integer, Integer> getOffsets() {
        return offsets;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "offsets=" + offsets +
                ", payload='" + payload + '\'' +
                '}';
    }
}
