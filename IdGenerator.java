import java.time.Instant;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final Random random = new Random();
    private static final String CHAR_POOL = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // Исключены 0,O,1,I

    // Типы объектов
    public enum ObjectType {
        ORDER("OR"),
        USER("US"),
        WORKER("WR"),
        WAREHOUSE("WH"),
        PRODUCER("PR");

        private final String prefix;

        ObjectType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public static String generateId(ObjectType type) {
        // Часть 1: Префикс типа объекта (2 символа)
        String prefix = type.getPrefix();

        // Часть 2: Текущая временная метка в base64 (6 символов)
        String timestamp = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(longToBytes(Instant.now().toEpochMilli()))
                .substring(0, 6);

        // Часть 3: Случайные символы (5 символов)
        StringBuilder randomPart = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            randomPart.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }

        // Часть 4: Инкрементный счетчик (3 символа в base36)
        String counterPart = Long.toString(counter.getAndIncrement(), 36)
                .toUpperCase();
        counterPart = String.format("%3s", counterPart).replace(' ', '0');

        return prefix + "-" + timestamp + "-" + randomPart + "-" + counterPart;
    }

    private static byte[] longToBytes(long value) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(value & 0xFF);
            value >>= 8;
        }
        return result;
    }
}