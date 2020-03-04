package Service;

import java.util.UUID;

public class GenerateID {
    public static String genID() {
        return UUID.randomUUID().toString();
    }
}
