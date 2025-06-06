import java.time.LocalTime;

/**
 * Класс Симуляции Времени (SimulationTime)
 *
 * Управляет виртуальным временем системы. Позволяет:
 * - Устанавливать текущее время симуляции
 * - Получать текущее виртуальное время
 * - Увеличивать время симуляции на заданное количество секунд
 *
 * Важно: Используется для синхронизации процессов в системе
 * вместо реального времени, что ускоряет тестирование.
 */
public class SimulationTime {
    // Текущее время симуляции (статическая переменная)
    private static LocalTime currentTime;

    /**
     * Установить текущее время симуляции
     * @param time новое время для симуляции
     */
    public static void setTime(LocalTime time) {
        currentTime = time;
    }

    /**
     * Получить текущее время симуляции
     * @return текущее виртуальное время
     */
    public static LocalTime getCurrentTime() {
        return currentTime;
    }

    /**
     * Добавить секунды к текущему времени симуляции
     * @param seconds количество секунд для добавления
     */
    public static void addSeconds(long seconds) {
        currentTime = currentTime.plusSeconds(seconds);
    }
}